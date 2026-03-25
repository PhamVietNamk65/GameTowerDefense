package asset;

public class AssetLoad {
    private MapAsset mapAsset;
    private UIAsset uiAsset;
    public AssetLoad() {
        mapAsset = MapAsset.getInstance();
        uiAsset = UIAsset.getInstance();
    }
    public void loadAllAssets() {
        mapAsset.load();
        uiAsset.load();
    }
}
