package com.jbroker.utils;

import static com.jbroker.utils.ByteUtils.readUnsignedByte;
import static com.jbroker.utils.ByteUtils.toArrayIndex;
import static com.jbroker.utils.ByteUtils.toUnsigned;

import com.jbroker.packet.MqttPacket;
import java.util.Arrays;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // This is the utility class
public class PacketParseUtils {

  public static final int STRING_LENGTH_OFFSET = 2;

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718016">1.5.3
   * UTF-8 encoded strings</a>
   */
  public static String readStringField(byte[] packetBuffer, int startBytePosition) {
    int lengthMostSignificantByte = readUnsignedByte(packetBuffer, startBytePosition);
    int lengthLeastSignificantByte = readUnsignedByte(packetBuffer, startBytePosition + 1);
    int startIndex = toArrayIndex(startBytePosition);
    byte[] stringBytes = Arrays.copyOfRange(
        packetBuffer,
        startIndex + lengthMostSignificantByte + STRING_LENGTH_OFFSET,
        startIndex + lengthLeastSignificantByte + STRING_LENGTH_OFFSET
    );
    return new String(stringBytes, MqttPacket.TEXT_FIELD_ENCODING);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718040">3.3.3
   * PUBLISH Payload</a>
   */
  public static String readApplicationMessage(
      byte[] packetBuffer,
      int startBytePosition,
      int remainingLength) {
    int startIndex = toArrayIndex(startBytePosition);
    int endIndex = toArrayIndex(remainingLength);
    if (endIndex < startIndex) {
      return ""; // It is valid for a PUBLISH Packet to contain a zero length payload
    }
    byte[] stringBytes = Arrays.copyOfRange(packetBuffer, startIndex, endIndex);
    return new String(stringBytes, MqttPacket.TEXT_FIELD_ENCODING);
  }

  /**
   * Combines two bytes into a single 16-bit integer.
   */
  public static int combineBytesToInt(byte mostSignificantByte, byte leastSignificantByte) {
    return (toUnsigned(mostSignificantByte) << 8) | toUnsigned(leastSignificantByte);
  }

  /**
   * Calculates the starting byte position for a field in an MQTT packet by taking into account the
   * length of all previous string fields. This is useful when parsing variable-length fields in the
   * packet, such as clientId, willTopic, userName, and password.
   *
   * <p>Each string field contributes its length plus {@code STRING_LENGTH_OFFSET}
   * to the total, which is added to {@code offsetPosition} to determine the next field's start
   * position.
   *
   * @param offsetPosition       The initial offset position to start from (e.g., the start position
   *                             of the clientId).
   * @param previousStringFields The variable-length string fields that come before the current
   *                             field, can be null (e.g., clientId, willTopic, userName).
   * @return The calculated starting byte position for the next field based on the offset and
   * previous field lengths.
   */
  public static int calculateStartBytePosition(int offsetPosition, String... previousStringFields) {
    int calculatedPosition = Arrays.stream(previousStringFields)
        .filter(Objects::nonNull)
        .mapToInt(previousStringField -> STRING_LENGTH_OFFSET + previousStringField.length())
        .sum();
    return offsetPosition + calculatedPosition;
  }

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
