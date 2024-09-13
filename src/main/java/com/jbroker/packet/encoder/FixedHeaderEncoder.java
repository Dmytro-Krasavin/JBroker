package com.jbroker.packet.encoder;

import static com.jbroker.packet.FixedHeader.CONTROL_PACKET_TYPE_POSITION;
import static com.jbroker.packet.FixedHeader.REMAINING_LENGTH_START_POSITION;
import static com.jbroker.packet.PublishFixedHeader.DUPLICATE_FLAG_BIT;
import static com.jbroker.packet.PublishFixedHeader.QOS_LEVEL_END_BIT;
import static com.jbroker.packet.PublishFixedHeader.QOS_LEVEL_START_BIT;
import static com.jbroker.packet.PublishFixedHeader.RETAIN_FLAG_BIT;
import static com.jbroker.utils.ByteUtils.modifyBit;
import static com.jbroker.utils.ByteUtils.toArrayIndex;

import com.jbroker.command.CommandType;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PublishFixedHeader;
import com.jbroker.packet.QosLevel;
import com.jbroker.utils.ArrayUtils;
import java.util.ArrayList;
import java.util.List;

public class FixedHeaderEncoder {

  public byte[] encode(FixedHeader fixedHeader) {
    return encode(encodeFirstByte(fixedHeader), fixedHeader.getRemainingLength());
  }

  public byte[] encode(PublishFixedHeader fixedHeader) {
    return encode(encodeFirstByte(fixedHeader), fixedHeader.getRemainingLength());
  }

  private byte[] encode(byte firstByte, int remainingLength) {
    List<Byte> fixedHeaderBytes = new ArrayList<>();
    fixedHeaderBytes.add(toArrayIndex(CONTROL_PACKET_TYPE_POSITION), firstByte);
    fixedHeaderBytes.addAll(
        toArrayIndex(REMAINING_LENGTH_START_POSITION),
        encodeRemainingLength(remainingLength)
    );
    return ArrayUtils.toByteArray(fixedHeaderBytes);
  }

  private byte encodeFirstByte(FixedHeader fixedHeader) {
    return encodeControlPacketType(fixedHeader.getCommandType());
  }

  private byte encodeFirstByte(PublishFixedHeader fixedHeader) {
    byte firstByte = encodeControlPacketType(fixedHeader.getCommandType());
    firstByte = setDuplicateFlag(firstByte, fixedHeader.isDuplicateFlag());
    firstByte = setQosLevel(firstByte, fixedHeader.getQosLevel());
    firstByte = setRetainFlag(firstByte, fixedHeader.isRetain());
    return firstByte;
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718021">2.2.1
   * MQTT Control Packet type</a>
   */
  private byte encodeControlPacketType(CommandType commandType) {
    return (byte) (commandType.getValue() << 4);
  }

  /**
   * Encodes a non-negative integer into the variable-length encoding scheme used by MQTT.
   * <p> @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718023">2.2.3
   * Remaining Length</a>
   *
   * @param remainingLength The non-negative integer to encode.
   * @return A list of bytes representing the encoded integer.
   */
  public static List<Byte> encodeRemainingLength(int remainingLength) {
    if (remainingLength < 0) {
      throw new IllegalArgumentException("Value cannot be negative");
    }

    List<Byte> encodedBytes = new ArrayList<>();

    do {
      int encodedByte = remainingLength % 128;  // Get the lower 7 bits
      remainingLength /= 128;  // Integer division by 128

      // If there are more bytes to encode, set the MSB to 1
      if (remainingLength > 0) {
        encodedByte |= 128;
      }

      encodedBytes.add((byte) encodedByte);
    } while (remainingLength > 0);

    return encodedBytes;
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.1
   * PUBLISH DUP flag</a>
   */
  private static byte setDuplicateFlag(byte firstByte, boolean duplicateFlag) {
    return modifyBit(firstByte, DUPLICATE_FLAG_BIT, duplicateFlag);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.2
   * PUBLISH QoS</a>
   */
  private static byte setQosLevel(byte firstByte, QosLevel qosLevel) {
    return switch (qosLevel) {
      case QOS_0 -> { // Ob00
        modifyBit(firstByte, QOS_LEVEL_END_BIT, false);
        modifyBit(firstByte, QOS_LEVEL_START_BIT, false);
        yield firstByte;
      }
      case QOS_1 -> { // Ob01
        modifyBit(firstByte, QOS_LEVEL_END_BIT, false);
        modifyBit(firstByte, QOS_LEVEL_START_BIT, true);
        yield firstByte;
      }
      case QOS_2 -> { // Ob10
        modifyBit(firstByte, QOS_LEVEL_END_BIT, true);
        modifyBit(firstByte, QOS_LEVEL_START_BIT, false);
        yield firstByte;
      }
    };
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.3
   * PUBLISH RETAIN</a>
   */
  private static byte setRetainFlag(byte firstByte, boolean retainFlag) {
    return modifyBit(firstByte, RETAIN_FLAG_BIT, retainFlag);
  }
}
