package Manager;

public class WaveManager {

    private int[][] waves;

    private int spawnDelay; // thời gian chờ giữa 2 lần spawn quái (tính bằng tick)
    private int waveDelay;  // thời gian chờ giữa 2 wave (tính bằng tick)

    private int currentWave = 0;  // index của wave hiện tại (bắt đầu từ 0)
    private int spawnIndex = 0; // index của quái tiếp theo trong wave hiện tại (bắt đầu từ 0)
    private int tickCounter = 0;    // đếm số tick đã trôi qua kể từ lần spawn cuối cùng hoặc kể từ khi bắt đầu wave
    private boolean waveActive = false; // flag báo hiệu đang trong quá trình spawn quái của wave hiện tại (true = đang spawn, false = đã spawn xong wave nhưng có thể
    private boolean waitingNext = false; // flag báo hiệu đang trong thời gian chờ giữa 2 wave
    private boolean spawnedAllEnemies = false; // flag báo hiệu đã spawn hết quái của wave hiện tại
    
    private boolean wavesDone = false;
    private EnemyManager enemyManager;

    private LevelManager levelManager;


    public WaveManager(EnemyManager enemyManager, LevelManager levelManager) {
        this.enemyManager = enemyManager;
        this.levelManager = levelManager;
        waves = levelManager.getCurrentLevelData().getWaves();
        spawnDelay = levelManager.getCurrentLevelData().getSpawnDelay();
        waveDelay = levelManager.getCurrentLevelData().getWaveDelay();

    }

    public void update() {
        if (wavesDone) return;

        tickCounter++;

        // Chưa bắt đầu wave nào
        if (!waveActive && !waitingNext && !spawnedAllEnemies) {
            startWave();
            return;
        }

        // 1. Nếu đang trong thời gian chờ giữa 2 wave
        if (waitingNext) {
            if (tickCounter >= waveDelay) { 
                tickCounter = 0;
                waitingNext = false;
                startWave();
            }
            return; 
        }

        // 2. Nếu đang trong quá trình sinh quái (Spawning)
        if (waveActive) {
            if (tickCounter >= spawnDelay) {
                tickCounter = 0;
                spawnNext();
            }
            return;
        }

        // 3. Nếu đã sinh hết quái nhưng vẫn còn quái trên màn hình (Cleanup)
        if (spawnedAllEnemies) {
            if (enemyManager.getMonsters().isEmpty()) {
                spawnedAllEnemies = false;
                currentWave++;

               if (currentWave >= waves.length) {
                    wavesDone = true;
                    System.out.println("Tất cả wave đã hoàn thành!");
                } else {
                    waitingNext = true; // Bắt đầu trạng thái chờ wave tiếp theo
                    tickCounter = 0;
                }
            }
        }
    }

    private void startWave() {
        if (currentWave >= waves.length) {
            wavesDone = true;
            return;
        }

        spawnIndex = 0;
        tickCounter = 0;
        waveActive = true;
        waitingNext = false;
        spawnedAllEnemies = false;

        spawnNext(); // spawn ngay con đầu tiên
    }

    private void spawnNext() {
        int[] wave = waves[currentWave];

        if (spawnIndex >= wave.length) {
            waveActive = false;
            spawnedAllEnemies = true;
            tickCounter = 0;
            return;
        }
        
        int type = wave[spawnIndex];
        int pathIndex = 0;

        if (levelManager.getCurrentLevel().getLevelID() == 3) {
            if (currentWave == 1 || currentWave == 3) {
                pathIndex = spawnIndex % 2; 
            }
        }
        enemyManager.spawnMonster(type,pathIndex);
        spawnIndex++;
        
    }

    public int getCurrentWave() {
        return Math.min(currentWave + 1, waves.length + 1);
    }

    public int getTotalWaves() {
        return waves.length + 1;
    }

    public boolean isWavesDone() {
        return wavesDone;
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public boolean isWaitingNext() {
        return waitingNext;
    }

    public int getSecondsUntilNext(int fps) {
        if (!waitingNext) return 0;
        int remaining = waveDelay - tickCounter;
        return Math.max(0, remaining / fps);
    }


}