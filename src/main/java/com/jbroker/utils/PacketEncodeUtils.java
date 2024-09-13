package com.jbroker.utils;

import static com.jbroker.utils.ByteUtils.toUnsigned;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // This is the utility class
public class PacketEncodeUtils {

  /**
   * Encodes a packet identifier into a byte array with the Most Significant Byte (MSB) and Least
   * Significant Byte (LSB).
   *
   * @param packetIdentifier The packet identifier as an integer.
   * @return A byte array where the first element is the MSB and the second element is the LSB.
   */
  public static byte[] encodePacketIdentifier(int packetIdentifier) {
    byte[] packetIdentifierBytes = new byte[2];
    packetIdentifierBytes[0] = getMSB(packetIdentifier);
    packetIdentifierBytes[1] = getLSB(packetIdentifier);
    return packetIdentifierBytes;
  }

  /**
   * Extracts the Most Significant Byte (MSB) from a 16-bit integer.
   *
   * @param value The 16-bit integer value.
   * @return The MSB of the value as a byte.
   */
  private static byte getMSB(int value) {
    return (byte) (value >> 8); // Right shift by 8 bits to get the upper byte (MSB)
  }

  /**
   * Extracts the Least Significant Byte (LSB) from a 16-bit integer.
   *
   * @param value The 16-bit integer value.
   * @return The LSB of the value as a byte.
   */
  private static byte getLSB(int value) {
    return (byte) toUnsigned((byte) value); // Mask to get the lower byte (LSB)
  }
}
