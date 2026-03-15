package main;
//trang thai cua game
public enum GameStates {
    PLAYING,
    LEVEL,
    MENU,
    SETTING;
    public static GameStates gameStates = MENU; // trang thai hien tai cua game
    
    public static GameStates getGameStates() {
        return gameStates;
    }
    public static void setGameStates(GameStates gameStates) {
        GameStates.gameStates = gameStates;
    }
}
