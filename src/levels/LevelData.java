package levels;
public class LevelData {
    private int[][] waves;
    private int spawnDelay;
    private int waveDelay;

    private int startGold;
    private int startLives;

    public LevelData(int[][] waves, int spawnDelay, int waveDelay, int startGold, int starLives) {
        this.waves = waves;
        this.spawnDelay = spawnDelay;
        this.waveDelay = waveDelay;
        this.startGold = startGold;
        this.startLives = starLives;
    }

    public int[][] getWaves() { 
        return waves; 
    }
    
    public int getSpawnDelay() { 
        return spawnDelay; 
    }

    public int getWaveDelay() { 
        return waveDelay; 
    }

    public int getStartGold() {
        return startGold;
    }

    public int getStartLives() {
        return startLives;
    }
    
    public int getMaxWaves() {
        return waves.length;
    }
}