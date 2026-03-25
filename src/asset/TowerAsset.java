package asset;

import java.awt.image.BufferedImage;
import java.lang.classfile.ClassFile.ConstantPoolSharingOption;

import entity.Tower;
import helpz.LoadSave;
import utils.Constants;

public class TowerAsset {
    private static TowerAsset instance;
    public static BufferedImage[][][] archerAnimations;
    public static TowerAsset getInstance() {
        if(instance == null) {
            instance = new TowerAsset();
        }
        return instance;
    }
    
    public void load(){
        loadArcherAnimations();
    }

    public void loadArcherAnimations() {
        archerAnimations = new BufferedImage[3][3][];
        archerAnimations[Tower.SIDE][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/S_Idle.png",      Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.SIDE][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/S_Preattack.png", Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.SIDE][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/S_Attack.png",    Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/U_Idle.png",      Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/U_Preattack.png", Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.UP  ][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/U_Attack.png",    Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.IDLE]      = LoadSave.getSpriteFrames("tower/3 Units/1/D_Idle.png",      Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.PREATTACK] = LoadSave.getSpriteFrames("tower/3 Units/1/D_Preattack.png", Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
        archerAnimations[Tower.DOWN][Tower.ATTACK]    = LoadSave.getSpriteFrames("tower/3 Units/1/D_Attack.png",    Constants.Towers.ARCHER, Constants.Towers.ARCHER_H);
    }
}
