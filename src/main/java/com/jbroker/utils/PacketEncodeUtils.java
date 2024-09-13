package com.jbroker.utils;

import static com.jbroker.utils.ByteUtils.toUnsigned;

import com.jbroker.packet.MqttPacket;
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
   * Encodes a text field into a byte array, where the first two bytes represent the length of the
   * text (in the Most Significant Byte (MSB) and Least Significant Byte (LSB) format), followed by
   * the UTF-8 encoded bytes of the text itself.
   *
   * <p>@see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718016">1.5.3
   * UTF-8 encoded strings</a>
   *
   * @param text The text to encode.
   * @return A byte array where the first two bytes represent the length of the text, followed by
   * the UTF-8
   */
  public static byte[] encodeTextField(String text) {
    byte[] textBytes = text.getBytes(MqttPacket.TEXT_FIELD_ENCODING);
    byte[] textLengthBytes = new byte[2];
    textLengthBytes[0] = getMSB(textBytes.length);
    textLengthBytes[1] = getLSB(textBytes.length);
    return ArrayUtils.mergeArrays(textLengthBytes, textBytes);
  }


  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718040">3.3.3
   * PUBLISH Payload</a>
   */
  public static byte[] encodeApplicationMessage(String applicationMessage) {
    return applicationMessage.getBytes(MqttPacket.TEXT_FIELD_ENCODING);
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
