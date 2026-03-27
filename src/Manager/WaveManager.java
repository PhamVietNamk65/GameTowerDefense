package Manager;

import static helpz.Constants.Monsters.*;

public class WaveManager {

    // Mỗi wave = danh sách loại quái
    private static final int[][] WAVES = {
        { SLIME, SLIME, SLIME, SLIME },
        { SLIME, SLIME, ORC, SLIME, ORC },
        { ORC, WOLF, ORC, WOLF, WOLF },
        { BEE, BEE, ORC, WOLF, BEE, ORC, WOLF },
        { ORC, ORC, WOLF, WOLF, BEE, BEE, ORC, WOLF, SLIME, SLIME }
    };

    private static final int SPAWN_DELAY = 90;
    private static final int WAVE_DELAY = 300;

    private int currentWave = 0;
    private int spawnIndex = 0;
    private int tickCounter = 0;

    private boolean waveActive = false;
    private boolean waitingNext = false;
    private boolean wavesDone = false;
    private boolean spawnedAllEnemies = false;

    private EnemyManager enemyManager;

    public WaveManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void update() {
        if (wavesDone) return;

        tickCounter++;

        // Chưa bắt đầu wave nào
        if (!waveActive && !waitingNext && !spawnedAllEnemies) {
            startWave();
            return;
        }

        // Đang chờ wave tiếp theo
        if (waitingNext) {
            if (tickCounter >= WAVE_DELAY) {
                tickCounter = 0;
                waitingNext = false;
                startWave();
            }
            return;
        }

        // Đang spawn trong wave hiện tại
        if (waveActive) {
            if (tickCounter >= SPAWN_DELAY) {
                tickCounter = 0;
                spawnNext();
            }
            return;
        }

        // Spawn xong hết quái -> chờ quái chết sạch / tới đích hết
        if (spawnedAllEnemies) {
            if (enemyManager.getMonsters().isEmpty()) {
                spawnedAllEnemies = false;
                currentWave++;

                if (currentWave >= WAVES.length) {
                    wavesDone = true;
                    System.out.println("Tất cả wave đã hoàn thành!");
                } else {
                    waitingNext = true;
                    tickCounter = 0;
                    System.out.println("Wave " + currentWave + " kết thúc. Chờ wave tiếp theo...");
                }
            }
        }
    }

    private void startWave() {
        if (currentWave >= WAVES.length) {
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
        int[] wave = WAVES[currentWave];

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
        return Math.min(currentWave + 1, WAVES.length);
    }

    public int getTotalWaves() {
        return WAVES.length;
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
        int remaining = WAVE_DELAY - tickCounter;
        return Math.max(0, remaining / fps);
    }
}