package asset;

public class AssetLoad {
    private MapAsset mapAsset;
    private UIAsset uiAsset;
    private TowerAsset towerAsset;
    private MonsterAsset monsterAsset;

    public AssetLoad() {
        mapAsset = MapAsset.getInstance();
        uiAsset = UIAsset.getInstance();
        towerAsset = TowerAsset.getInstance();
        monsterAsset = MonsterAsset.getInstance();
    }

    public void loadAllAssets() {
        mapAsset.load();
        uiAsset.load();
        towerAsset.load();
        monsterAsset.load();
    }
}
