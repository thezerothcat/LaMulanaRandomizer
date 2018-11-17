package lmr.randomizer.node;

/**
 * Created by thezerothcat on 8/5/2017.
 */
public class AnkhJewelLockChecker implements Runnable {
    private AccessChecker accessChecker;

    public AnkhJewelLockChecker(AccessChecker accessChecker, String bossEvent) {
        this.accessChecker = accessChecker;
        accessChecker.markBossDefeated(bossEvent);
    }

    @Override
    public void run() {
        while (!accessChecker.getQueuedUpdates().isEmpty()) {
            accessChecker.computeAccessibleNodes(accessChecker.getQueuedUpdates().iterator().next(), null);
        }
    }

    public boolean isEnoughAnkhJewelsToDefeatAllAccessibleBosses() {
        return accessChecker.isEnoughAnkhJewelsToDefeatAllAccessibleBosses();
    }
}
