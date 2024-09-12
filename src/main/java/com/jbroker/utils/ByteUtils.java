package com.jbroker.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // This is the utility class
public class ByteUtils {

  public static boolean isBitSet(byte byteValue, int position) {
    return ((byteValue >> position) & 1) == 1;
  }

  public static byte modifyBit(byte byteValue, int bitPosition, boolean setBit) {
    if (bitPosition < 0 || bitPosition > 7) {
      throw new IllegalArgumentException("Bit position must be between 0 and 7");
    }
    // Create a mask with the bit at the specified position set to 1
    byte mask = (byte) (1 << bitPosition);

    if (setBit) {
      // Set the bit using bitwise OR operation
      return (byte) (byteValue | mask);
    }
    // Clear the bit using bitwise AND operation with the inverse of the mask
    return (byte) (byteValue & ~mask);
  }

  public static int toUnsigned(byte value) {
    return value & 0xFF;
  }

  public static byte readByte(byte[] buffer, int bytePosition) {
    return buffer[toArrayIndex(bytePosition)];
  }

  public static int readUnsignedByte(byte[] buffer, int bytePosition) {
    return toUnsigned(buffer[toArrayIndex(bytePosition)]);
  }

  public static int toArrayIndex(int bytePosition) {
    return bytePosition - 1;
  }
}
