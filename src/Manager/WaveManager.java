package Manager;

import static utils.Constants.Monsters.*;

public class WaveManager {
    private enum WaveState {
        SPAWNING,
        WAITING_NEXT,
        CLEANUP,
        DONE
    }
    private WaveState state = WaveState.SPAWNING;
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

    private EnemyManager enemyManager;

    public WaveManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void update() {
        if (wavesDone) return;

        tickCounter++;

        switch (state) {

            case SPAWNING:
                if (tickCounter >= SPAWN_DELAY) {
                    tickCounter = 0;
                    spawnNext();
                }
                break;

            case CLEANUP:
                if (enemyManager.getMonsters().isEmpty()) {
                    currentWave++;

                    if (currentWave >= WAVES.length) {
                        wavesDone = true;
                        state = WaveState.DONE;
                    } else {
                        state = WaveState.WAITING_NEXT;
                        tickCounter = 0;
                    }
                }
                break;

            case WAITING_NEXT:
                if (tickCounter >= WAVE_DELAY) {
                    startWave();
                }
                break;

            case DONE:
                break;
        }
    }

    private void startWave() {
        spawnIndex = 0;
        tickCounter = 0;
        state = WaveState.SPAWNING;

        System.out.println("Wave " + (currentWave + 1) + " bắt đầu!");
        spawnNext();
    }

    private void spawnNext() {
        int[] wave = WAVES[currentWave];

        if (spawnIndex >= wave.length) {
            state = WaveState.CLEANUP;
            tickCounter = 0;
            return;
        }

        enemyManager.addMonster(wave[spawnIndex]);
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