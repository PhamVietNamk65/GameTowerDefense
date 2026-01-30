package main;
//trang thai cua game
public enum GameStates {
    PLAYING,
    MENU,
    SETTING;

    public static GameStates gameStates = MENU;

    public static void SetGameState(GameStates state){
        gameStates = state;
    }
}
