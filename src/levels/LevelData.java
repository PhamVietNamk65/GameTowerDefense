package levels;
public class LevelData {
    private int[][] waves;
    private int spawnDelay;
    private int waveDelay;

    public LevelData(int[][] waves, int spawnDelay, int waveDelay) {
        this.waves = waves;
        this.spawnDelay = spawnDelay;
        this.waveDelay = waveDelay;
    }

    // Getters
    public int[][] getWaves() { return waves; }
    public int getSpawnDelay() { return spawnDelay; }
    public int getWaveDelay() { return waveDelay; }
    
}