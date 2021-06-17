package lmr.randomizer.rcd.object;

public enum DropType {
    NOTHING(0),
    COINS(1),
    WEIGHTS(2),
    SHURIKEN_AMMO(3),
    ROLLING_SHURIKEN_AMMO(4),
    EARTH_SPEAR_AMMO(5),
    FLARE_GUN_AMMO(6),
    BOMB_AMMO(7),
    CHAKRAM_AMMO(8),
    CALTROPS_AMMO(9),
    PISTOL_AMMO(10),
    RANDOM_COINS_WEIGHTS_SOUL(11),
    SOUL(12); // Pots cannot drop soul.

    private short value;

    DropType(int value) {
        this.value = (short)value;
    }

    public short getValue() {
        return value;
    }
}
