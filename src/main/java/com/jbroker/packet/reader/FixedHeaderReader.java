package com.jbroker.packet.reader;

import static com.jbroker.packet.model.header.PublishFixedHeader.DUPLICATE_FLAG_BIT;
import static com.jbroker.packet.model.header.PublishFixedHeader.QOS_LEVEL_END_BIT;
import static com.jbroker.packet.model.header.PublishFixedHeader.QOS_LEVEL_START_BIT;
import static com.jbroker.packet.model.header.PublishFixedHeader.RETAIN_FLAG_BIT;
import static com.jbroker.packet.model.inbound.impl.SubscribePacket.SUBSCRIBE_FIXED_HEADER_BYTE;
import static com.jbroker.packet.model.inbound.impl.UnsubscribePacket.UNSUBSCRIBE_FIXED_HEADER_BYTE;

import com.jbroker.command.CommandType;
import com.jbroker.exception.MalformedPacketException;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.header.PublishFixedHeader;
import com.jbroker.packet.model.QosLevel;
import com.jbroker.utils.ByteUtils;
import java.io.IOException;
import java.io.InputStream;

public class FixedHeaderReader {

  public FixedHeader read(int firstByte, InputStream input) throws IOException {
    int controlPacketType = readControlPacketType(firstByte);
    int remainingLength = readRemainingLength(input);

    if (controlPacketType == CommandType.PUBLISH.getValue()) {
      return readPublishFixedHeader((byte) firstByte, remainingLength);
    }
    if (controlPacketType == CommandType.SUBSCRIBE.getValue()) {
      validateSubscribeFixedHeader(firstByte);
    }
    if (controlPacketType == CommandType.UNSUBSCRIBE.getValue()) {
      validateUnsubscribeFixedHeader(firstByte);
    }

    return new FixedHeader(controlPacketType, remainingLength);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718021">2.2.1
   * MQTT Control Packet type</a>
   */
  private int readControlPacketType(int firstByte) {
    return firstByte >> 4;
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718023">2.2.3
   * Remaining Length</a>
   */
  private int readRemainingLength(InputStream input) throws IOException {
    int multiplier = 1;
    int value = 0;
    int encodedByte;
    do {
      encodedByte = input.read();
      value += (encodedByte & Byte.MAX_VALUE) * multiplier;
      multiplier *= 128;
      if (multiplier > 128 * 128 * 128) {
        throw new IOException("Malformed Remaining Length");
      }
    } while ((encodedByte & 128) != 0);
    return value;
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1
   * PUBLISH Fixed header</a>
   */
  private static PublishFixedHeader readPublishFixedHeader(byte firstByte, int remainingLength) {
    boolean duplicateFlag = readDuplicateFlag(firstByte);
    QosLevel qosLevel = readQosLevel(firstByte);
    boolean retain = readRetainFlag(firstByte);
    return new PublishFixedHeader(remainingLength, duplicateFlag, qosLevel, retain);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.1
   * PUBLISH DUP flag</a>
   */
  private static boolean readDuplicateFlag(byte firstByte) {
    return ByteUtils.isBitSet(firstByte, DUPLICATE_FLAG_BIT);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.2
   * PUBLISH QoS</a>
   */
  private static QosLevel readQosLevel(byte firstByte) {
    return QosLevel.resolveQoS(
        ByteUtils.combineBits(firstByte, QOS_LEVEL_START_BIT, QOS_LEVEL_END_BIT)
    );
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.3
   * PUBLISH RETAIN</a>
   */
  private static boolean readRetainFlag(byte firstByte) {
    return ByteUtils.isBitSet(firstByte, RETAIN_FLAG_BIT);
  }

  /**
   * From MQTT specification: <i>Bits 3,2,1 and 0 of the fixed header of the SUBSCRIBE Control
   * Packet are reserved and MUST be set to 0,0,1 and 0 respectively. The Server MUST treat any
   * other value as malformed and close the Network Connection</i>
   *
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718064">3.8.1
   * SUBSCRIBE Fixed header</a>
   */
  private void validateSubscribeFixedHeader(int firstByte) {
    validateFixedHeader(firstByte, SUBSCRIBE_FIXED_HEADER_BYTE, CommandType.SUBSCRIBE);
  }

  /**
   * From MQTT specification: <i>Bits 3,2,1 and 0 of the fixed header of the UNSUBSCRIBE Control
   * Packet are reserved and MUST be set to 0,0,1 and 0 respectively. The Server MUST treat any
   * other value as malformed and close the Network Connection</i>
   *
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718073">3.10.1
   * UNSUBSCRIBE Fixed header</a>
   */
  private void validateUnsubscribeFixedHeader(int firstByte) {
    validateFixedHeader(firstByte, UNSUBSCRIBE_FIXED_HEADER_BYTE, CommandType.UNSUBSCRIBE);
  }

  private static void validateFixedHeader(
      int actualFixedHeaderByte,
      int expectedFixedHeaderByte,
      CommandType commandType) {
    if (actualFixedHeaderByte != expectedFixedHeaderByte) {
      throw new MalformedPacketException(
          commandType,
          String.format(
              "Malformed %s Fixed Header. Expected 0x%x but received 0x%x",
              commandType.name(), expectedFixedHeaderByte, actualFixedHeaderByte
          )
      );
    }
  }
}
