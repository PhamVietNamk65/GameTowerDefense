package asset;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import entity.monster.EnemyState;
import helpz.LoadSave;
import utils.Constants;
import static utils.Constants.Monsters.*;

public class MonsterAsset {
    private static MonsterAsset instance;

    public static Map<Integer, Map<EnemyState, Map<Integer, BufferedImage[]>>> enemyAnimations = new HashMap<>();

    public static MonsterAsset getInstance() {
        if (instance == null) {
            instance = new MonsterAsset();
        }
        return instance;
    }

    public void load() {
        loadEnemyAnimation(SLIME,
            "enemies/1/U_Walk.png",
            "enemies/1/D_Walk.png",
            "enemies/1/S_Walk.png",
            "enemies/1/U_Death.png",
            "enemies/1/D_Death.png",
            "enemies/1/S_Death.png",
            null,null,null);

        loadEnemyAnimation(ORC,
            "enemies/2/U_Walk.png",
            "enemies/2/D_Walk.png",
            "enemies/2/S_Walk.png",
            "enemies/2/U_Death.png",
            "enemies/2/D_Death.png",
            "enemies/2/S_Death.png",
            "enemies/2/U_Attack.png",
            "enemies/2/D_Attack.png",
            "enemies/2/S_Attack.png");

        loadEnemyAnimation(WOLF,
            "enemies/3/U_Walk.png",
            "enemies/3/D_Walk.png",
            "enemies/3/S_Walk.png",
            "enemies/3/U_Death.png",
            "enemies/3/D_Death.png",
            "enemies/3/S_Walk.png",
            "enemies/3/U_Attack.png",
            "enemies/3/D_Attack.png",
            "enemies/3/S_Attack.png");

        loadEnemyAnimation(BEE,
            "enemies/4/U_Walk.png",
            "enemies/4/D_Walk.png",
            "enemies/4/S_Walk.png",
            "enemies/4/U_Death.png",
            "enemies/4/D_Death.png",
            "enemies/4/S_Death.png",
            null,null,null);

        loadEnemyAnimation(RAT,
            "enemies/5/U_Run.png",
            "enemies/5/D_Run.png",
            "enemies/5/S_Run.png",
            "enemies/5/U_Death.png",
            "enemies/5/D_Death.png",
            "enemies/5/S_Death.png",
            "enemies/5/U_Attack.png",
            "enemies/5/D_Attack.png",
            "enemies/5/S_Attack.png");

        loadEnemyAnimation(RIDER,
            "enemies/6/U_Run.png",
            "enemies/6/D_Run.png",
            "enemies/6/S_Run.png",
            "enemies/6/U_Death.png",
            "enemies/6/D_Death.png",
            "enemies/6/S_Death.png",
            "enemies/6/U_Attack.png",
            "enemies/6/D_Attack.png",
            "enemies/6/S_Attack.png");


    }

    private void loadEnemyAnimation(int type,
            String walkUp, String walkDown, String walkSide,
            String deathUp, String deathDown, String deathSide,
            String attackUp, String attackDown, String attackSide) {

        Map<EnemyState, Map<Integer, BufferedImage[]>> stateMap = new HashMap<>();

        stateMap.putIfAbsent(EnemyState.DYING, new HashMap<>());
    	stateMap.get(EnemyState.DYING).put(Constants.Direction.UP, LoadSave.getSpriteFrames(deathUp, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        stateMap.get(EnemyState.DYING).put(Constants.Direction.DOWN, LoadSave.getSpriteFrames(deathUp, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        stateMap.get(EnemyState.DYING).put(Constants.Direction.LEFT, LoadSave.getSpriteFrames(deathSide, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
      
        // ===== WALK =====
        stateMap.put(EnemyState.WALK, new HashMap<>());
        stateMap.get(EnemyState.WALK).put(Constants.Direction.UP,    LoadSave.getSpriteFrames(walkUp,   type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        stateMap.get(EnemyState.WALK).put(Constants.Direction.DOWN,  LoadSave.getSpriteFrames(walkDown, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        stateMap.get(EnemyState.WALK).put(Constants.Direction.LEFT,  LoadSave.getSpriteFrames(walkSide, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        stateMap.get(EnemyState.WALK).put(Constants.Direction.RIGHT, LoadSave.getSpriteFrames(walkSide, type < 4 ? 48 : 96, type < 4 ? 48 : 96));

        // ===== ATTACK =====
        stateMap.put(EnemyState.ATTACK, new HashMap<>());
        if (attackUp != null) {

            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.UP,LoadSave.getSpriteFrames(attackUp, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.DOWN,LoadSave.getSpriteFrames(attackDown, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.LEFT,LoadSave.getSpriteFrames(attackSide, type < 4 ? 48 : 96, type < 4 ? 48 : 96)  );
            stateMap.get(EnemyState.ATTACK).put(Constants.Direction.RIGHT, LoadSave.getSpriteFrames(attackSide, type < 4 ? 48 : 96, type < 4 ? 48 : 96));
        } else {
            // Không có ảnh attack riêng → dùng WALK
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

        // Fallback về UP nếu hướng cụ thể không có
        if (frames == null) {
            return directionMap.get(Constants.Direction.UP);
        }

        return frames;
    }
}