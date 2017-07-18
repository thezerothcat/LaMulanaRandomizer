package lmr.randomizer.rcd;

/**
 * Created by thezerothcat on 7/17/2017.
 */
public enum TestOp {
    FLAG_EQUALS(0x00), // 11
    FLAG_LTEQ(0x01), // 12
    FLAG_GTEQ(0x02),// 13
    FLAG_AND_ISNZ(0x03), // 17
    FLAG_OR_ISNZ(0x04), // 18
    FLAG_XOR_ISNZ(0x05), // 19
    FLAG_IS_ZERO(0x06), // 20 - Bugged, always true
    FLAG_NOT_EQUAL(0x40), // 14
    FLAG_GT(0x41), // 15
    FLAG_LT(0x42), // 16
    FLAG_AND_IS_ZERO(0x43), // 21
    FLAG_OR_IS_ZERO(0x44), // 22
    FLAG_XOR_IS_ZERO(0x45), // 23 - Bugged and always true
    FLAG_IS_NON_ZERO(0x46); // 24

    private int op;

    TestOp(int op) {
        this.op = op;
    }

    public static TestOp get(int op) {
        for(TestOp value : TestOp.values()) {
            if(op == value.op) {
                return value;
            }
        }
        return null;
    }
}
