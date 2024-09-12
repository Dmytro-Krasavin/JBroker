package com.jbroker.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // This is the utility class
public class ByteUtils {

  public static boolean isBitSet(byte value, int position) {
    return ((value >> position) & 1) == 1;
  }

  public static int toUnsigned(byte value) {
    return value & 0xFF;
  }

  public static byte readByte(byte[] buffer, int bytePosition) {
    return buffer[bytePositionToArrayIndex(bytePosition)];
  }

  public static int readUnsignedByte(byte[] buffer, int bytePosition) {
    return toUnsigned(buffer[bytePositionToArrayIndex(bytePosition)]);
  }

  public static int bytePositionToArrayIndex(int bytePosition) {
    return bytePosition - 1;
  }
}
