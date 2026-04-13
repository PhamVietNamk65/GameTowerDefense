package asset;

public class AssetLoad {
    private MapAsset              mapAsset;
    private UIAsset               uiAsset;
    private TowerAsset            towerAsset;
    private MonsterAsset          monsterAsset;
    private WallAsset             wallAsset;    
    private CanonAsset            canonAsset;
    private WirzardFlameAsset     wirzardFlameAsset;
    private WirzardFrostAsset     wirzardFrostAsset;
    private WirzardLightningAsset wirzardLightningAsset;
    private SniperAsset           sniperAsset;          // ← NEW

    public AssetLoad() {
        mapAsset              = MapAsset.getInstance();
        uiAsset               = UIAsset.getInstance();
        towerAsset            = TowerAsset.getInstance();
        monsterAsset          = MonsterAsset.getInstance();
        canonAsset            = CanonAsset.getInstance();
        wirzardFlameAsset     = WirzardFlameAsset.getInstance();
        wirzardFrostAsset     = WirzardFrostAsset.getInstance();
        wirzardLightningAsset = WirzardLightningAsset.getInstance();
        sniperAsset           = SniperAsset.getInstance();  // ← NEW
        wallAsset             = WallAsset.getInstance();
    }

    public void loadAllAssets() {
        mapAsset.load();
        uiAsset.load();
        towerAsset.load();
        monsterAsset.load();
        wallAsset.load();
        canonAsset.load();
        wirzardFlameAsset.load();
        wirzardFrostAsset.load();
        wirzardLightningAsset.load();
        sniperAsset.load();
    }
}