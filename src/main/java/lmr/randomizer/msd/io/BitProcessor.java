package lmr.randomizer.msd.io;

// Big-endian bit producer and consumer, similar to a ByteBuffer but for bits. May be useful for non-MSD parsing as well.
public class BitProcessor {
    public int value;
    public byte position;

    public BitProcessor(int initialValue) {
        this.value = initialValue;
    }

    // Returns the bitCount bits left of the current position, then increments (moves left) the position by bitCount.
    public int get(byte bitCount) {
        var res = get(position, bitCount);
        position += bitCount;
        return res;
    }

    public int get(byte index, byte bitCount) {
        var mask = maxValue(bitCount) << index;
        return (value & mask) >>> index;
    }

    public long getUnsigned(byte bitCount) {
        var res = getUnsigned(position, bitCount);
        position += bitCount;
        return res;
    }

    public long getUnsigned(byte index, byte bitCount) {
        return Integer.toUnsignedLong(get(index, bitCount));
    }

    public BitProcessor put(int value, byte bitCount) {
        var res = put(position, value, bitCount);
        position += bitCount;
        return res;
    }

    public BitProcessor put(byte index, int value, byte bitCount) {
        this.value &= ~(maxValue(bitCount) << index); // zero the bits we are about to write to
        this.value |= clampToBits(value, bitCount) << index; // write the new value
        return this;
    }

    // returns the maximum int representable within bitCount bits.
    private static int maxValue(byte bitCount) {
        return (int) Math.pow(2, bitCount) - 1;
    }

    // clamps value between 0 and the max value for the provided bitCount;
    private static int clampToBits(int value, byte bitCount) {
        return Math.max(
            Math.min(value, maxValue(bitCount)),
            0
        );
    }
}
