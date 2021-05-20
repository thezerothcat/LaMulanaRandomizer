package lmr.randomizer.rcd.object;

/**
 * Created by thezerothcat on 7/18/2017.
 */
public enum  ByteOp {
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
    FLAG_IS_NON_ZERO(0x46), // 24

    ASSIGN_FLAG(0x00), // 1
    ADD_FLAG(0x01), // 7
    SUB_FLAG(0x02),// 8
    MULT_FLAG(0x03), // 9
    DIV_FLAG(0x04), // 10
    AND_FLAG(0x05), // 4
    OR_FLAG(0x06), // 5
    XOR_FLAG(0x07); // 6

    private int op;

    ByteOp(int op) {
        this.op = op;
    }

    public int getOp() {
        return op;
    }

    public static ByteOp getTestOp(int input) {
        if(input == 0x00) {
            return ByteOp.FLAG_EQUALS;
        }
        if(input == 0x01) {
            return ByteOp.FLAG_LTEQ;
        }
        if(input == 0x02) {
            return ByteOp.FLAG_GTEQ;
        }
        if(input == 0x03) {
            return ByteOp.FLAG_AND_ISNZ;
        }
        if(input == 0x04) {
            return ByteOp.FLAG_OR_ISNZ;
        }
        if(input == 0x05) {
            return ByteOp.FLAG_XOR_ISNZ;
        }
        if(input == 0x06) {
            return ByteOp.FLAG_IS_ZERO;
        }
        if(input == 0x40) {
            return ByteOp.FLAG_NOT_EQUAL;
        }
        if(input == 0x41) {
            return ByteOp.FLAG_GT;
        }
        if(input == 0x42) {
            return ByteOp.FLAG_LT;
        }
        if(input == 0x43) {
            return ByteOp.FLAG_AND_IS_ZERO;
        }
        if(input == 0x44) {
            return ByteOp.FLAG_OR_IS_ZERO;
        }
        if(input == 0x45) {
            return ByteOp.FLAG_XOR_IS_ZERO;
        }
        if(input == 0x46) {
            return ByteOp.FLAG_IS_NON_ZERO;
        }
        return null;
    }

    public static ByteOp getWriteOp(int input) {
        if(input == 0) {
            return ByteOp.ASSIGN_FLAG;
        }
        if(input == 1) {
            return ByteOp.ADD_FLAG;
        }
        if(input == 2) {
            return ByteOp.SUB_FLAG;
        }
        if(input == 3) {
            return ByteOp.MULT_FLAG;
        }
        if(input == 4) {
            return ByteOp.DIV_FLAG;
        }
        if(input == 5) {
            return ByteOp.AND_FLAG;
        }
        if(input == 6) {
            return ByteOp.OR_FLAG;
        }
        if(input == 7) {
            return ByteOp.XOR_FLAG;
        }
        return null;
    }

    @Override
    public String toString() {
        if(op == FLAG_EQUALS.getOp()) {
            return " == ";
        }
        if(op == FLAG_LTEQ.getOp()) {
            return " <= ";
        }
        if(op == FLAG_GTEQ.getOp()) {
            return " >= ";
        }
        if(op == FLAG_AND_ISNZ.getOp()) {
            return " FLAG_AND_ISNZ ";
        }
        if(op == FLAG_OR_ISNZ.getOp()) {
            return " FLAG_OR_ISNZ ";
        }
        if(op == FLAG_XOR_ISNZ.getOp()) {
            return " FLAG_XOR_ISNZ ";
        }
        if(op == FLAG_IS_ZERO.getOp()) {
            return " FLAG_IS_ZERO ";
        }
        if(op == FLAG_NOT_EQUAL.getOp()) {
            return " != ";
        }
        if(op == FLAG_GT.getOp()) {
            return "  > ";
        }
        if(op == FLAG_LT.getOp()) {
            return "  < ";
        }
        if(op == FLAG_AND_IS_ZERO.getOp()) {
            return " FLAG_AND_IS_ZERO ";
        }
        if(op == FLAG_OR_IS_ZERO.getOp()) {
            return " FLAG_OR_IS_ZERO ";
        }
        if(op == FLAG_XOR_IS_ZERO.getOp()) {
            return " FLAG_XOR_IS_ZERO ";
        }
        if(op == FLAG_IS_NON_ZERO.getOp()) {
            return " FLAG_IS_NON_ZERO ";
        }
        if(op == ASSIGN_FLAG.getOp()) {
            return "  = ";
        }
        if(op == ADD_FLAG.getOp()) {
            return " += ";
        }
        if(op == SUB_FLAG.getOp()) {
            return " -= ";
        }
        if(op == MULT_FLAG.getOp()) {
            return " *= ";
        }
        if(op == DIV_FLAG.getOp()) {
            return " /= ";
        }
        if(op == AND_FLAG.getOp()) {
            return " AND_FLAG ";
        }
        if(op == OR_FLAG.getOp()) {
            return " OR_FLAG ";
        }
        if(op == XOR_FLAG.getOp()) {
            return " XOR_FLAG ";
        }
        return "(unknown)";
    }
}
