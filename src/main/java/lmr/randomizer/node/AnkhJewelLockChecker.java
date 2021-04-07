package lmr.randomizer.node;

import lmr.randomizer.Settings;

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

    public boolean isAnkhJewelLock() {
        if(accessChecker.isEnoughAnkhJewelsToDefeatAllAccessibleBosses()) {
            return false;
        }
        if(!Settings.isFoolsGameplay()) {
            // Not enough jewels for all bosses, and all bosses are required.
            return true;
        }
        // All bosses are not required, so it's only ankh jewel lock if we can't win from this state.
        return !accessChecker.isSuccess(null);
    }

    public void logAnkhJewelLock() {
        accessChecker.logAnkhJewelLock();
    }
}
