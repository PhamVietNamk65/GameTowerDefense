package asset;
import static utils.Constants.Monsters.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import entity.monster.EnemyState;
import helpz.LoadSave;
import utils.Constants;
public class MonsterAsset {
    private static MonsterAsset instance;
    // Mỗi loại enemy có 1 mảng frame riêng
    public static Map<Integer, Map<EnemyState, Map<Integer,BufferedImage[]>>> enemyAnimations = new HashMap<>();

    public static MonsterAsset getInstance() {
        if(instance == null) {
            instance = new MonsterAsset();
        }
        return instance;
    }
    public void load(){
        loadEnemyAnimation(SLIME,
            "enemies/1/U_Walk.png",
            "enemies/1/D_Walk.png",
            "enemies/1/S_Walk.png",
            "enemies/1/U_Death.png",
            "enemies/1/D_Death.png",
            "enemies/1/S_Death.png",
            null);

    	loadEnemyAnimation(ORC,
            "enemies/2/U_Walk.png",
            "enemies/2/D_Walk.png",
            "enemies/2/S_Walk.png",
            "enemies/2/U_Death.png",
            "enemies/2/D_Death.png",
            "enemies/2/S_Death.png",
            "enemies/2/S_Attack.png");

    	loadEnemyAnimation(WOLF,
            "enemies/3/U_Walk.png",
            "enemies/3/D_Walk.png",
            "enemies/3/S_Walk.png",
            "enemies/3/U_Death.png",
            "enemies/3/D_Death.png",
            "enemies/3/S_Death.png",
        "enemies/3/U_Attack.png");

    	loadEnemyAnimation(BEE,
            "enemies/4/U_Walk.png",
            "enemies/4/D_Walk.png",
            "enemies/4/S_Walk.png",
            "enemies/4/U_Death.png",
            "enemies/4/D_Death.png",
            "enemies/4/S_Death.png",
            null);
    }

    private void loadEnemyAnimation(int type, String walkUp, String walkDown, String walkLeft,
        String deathUp, String deathDown, String deathLeft,
        String attackPath) {
    	Map<EnemyState,Map<Integer,BufferedImage[]> > stateMap = new HashMap<>();

    	stateMap.putIfAbsent(EnemyState.WALK, new HashMap<>());
        stateMap.get(EnemyState.WALK).put(Constants.Direction.UP, LoadSave.getSpriteFrames(walkUp, 48, 48));
        stateMap.get(EnemyState.WALK).put(Constants.Direction.DOWN, LoadSave.getSpriteFrames(walkDown, 48, 48));
        stateMap.get(EnemyState.WALK).put(Constants.Direction.LEFT, LoadSave.getSpriteFrames(walkLeft, 48, 48));

        stateMap.putIfAbsent(EnemyState.DYING, new HashMap<>());
    	stateMap.get(EnemyState.DYING).put(Constants.Direction.UP, LoadSave.getSpriteFrames(deathUp, 48, 48));
        stateMap.get(EnemyState.DYING).put(Constants.Direction.DOWN, LoadSave.getSpriteFrames(deathUp, 48, 48));
        stateMap.get(EnemyState.DYING).put(Constants.Direction.DOWN, LoadSave.getSpriteFrames(deathUp, 48, 48));

        stateMap.put(EnemyState.ATTACK, new HashMap<>());
        if (attackPath != null) {
            BufferedImage[] attackFrames = LoadSave.getSpriteFrames(attackPath, 48, 48);
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.UP, attackFrames);
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.DOWN, attackFrames);
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.LEFT, attackFrames);
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.RIGHT, attackFrames);
        } else {
            // Nếu không có ảnh attack riêng, dùng ảnh WALK của đúng hướng đó
            stateMap.get(EnemyState.ATTACK).putAll(stateMap.get(EnemyState.WALK));
        }
        enemyAnimations.put(type, stateMap);
	}
    
    public static BufferedImage[] getFrames(int type, EnemyState state, int direction) {
        Map<EnemyState, Map<Integer, BufferedImage[]>> stateMap = enemyAnimations.get(type);
        if (stateMap == null) return null;

        Map<Integer, BufferedImage[]> directionMap = stateMap.get(state);
        if (directionMap == null) return null;

        BufferedImage[] frames = directionMap.get(direction);

        return frames;
    }
}
