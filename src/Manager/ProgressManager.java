    package Manager;

    import levels.LevelNode;

    public class ProgressManager {

        private LevelNode[] levels;

        public ProgressManager(int totalLevels) {
            levels = new LevelNode[totalLevels + 1];

            for (int i = 1; i <= totalLevels; i++) {
                levels[i] = new LevelNode(i, false);
            }

            // mở level đầu tiên
            levels[1].unlock();
            levels[2].unlock();
        }

        public boolean isLevelUnlocked(int levelId) {
            if (levelId < 1 || levelId >= levels.length) return false;
            return levels[levelId].isUnlocked();
        }

        public void unlockNextLevel(int currentLevelId) {
            if (currentLevelId + 1 < levels.length) {
                levels[currentLevelId + 1].unlock();
            }
        }

        public LevelNode[] getLevels() {
            return levels;
        }
    }