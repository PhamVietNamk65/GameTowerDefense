package Manager;

import helpz.LoadLevelData;
import levels.LevelData;

public class WaveManager {

    private int[][] waves;

    private int spawnDelay;
    private int waveDelay;

    private int currentWave = 0;
    private int spawnIndex = 0;
    private int tickCounter = 0;

    private boolean waveActive = false;
    private boolean waitingNext = false;
    private boolean wavesDone = false;
    private boolean spawnedAllEnemies = false;

    private EnemyManager enemyManager;

    private LevelData currentLevel;

    public WaveManager(EnemyManager enemyManager, LevelData levelData) {
        this.enemyManager = enemyManager;
        this.currentLevel = levelData;
        waves = currentLevel.getWaves();

        spawnDelay = currentLevel.getSpawnDelay();
        waveDelay = currentLevel.getWaveDelay();

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

        System.out.println("Wave " + (currentWave + 1) + " bắt đầu!");
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
        enemyManager.spawnMonster(type);
        spawnIndex++;
    }

    public int getCurrentWave() {
        return Math.min(currentWave + 1, waves.length);
    }

    public int getTotalWaves() {
        return waves.length;
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