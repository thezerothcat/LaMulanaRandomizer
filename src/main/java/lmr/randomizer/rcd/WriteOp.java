package lmr.randomizer.rcd;

/**
 * Created by thezerothcat on 7/17/2017.
 */
public enum WriteOp {
    ASSIGN_FLAG(0), // 1
    ADD_FLAG(1), // 7
    SUB_FLAG(2),// 8
    MULT_FLAG(3), // 9
    DIV_FLAG(4), // 10
    AND_FLAG(5), // 4
    OR_FLAG(6), // 5
    XOR_FLAG(7); // 6

    private int op;

    WriteOp(int op) {
        this.op = op;
    }

    public static WriteOp get(int op) {
        for(WriteOp value : WriteOp.values()) {
            if(op == value.op) {
                return value;
            }
        }
        return null;
    }
}
