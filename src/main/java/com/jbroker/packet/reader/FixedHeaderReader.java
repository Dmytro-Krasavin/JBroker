package com.jbroker.packet.reader;

import static com.jbroker.packet.PublishFixedHeader.DUPLICATE_FLAG_BIT;
import static com.jbroker.packet.PublishFixedHeader.QOS_LEVEL_END_BIT;
import static com.jbroker.packet.PublishFixedHeader.QOS_LEVEL_START_BIT;
import static com.jbroker.packet.PublishFixedHeader.RETAIN_FLAG_BIT;

import com.jbroker.command.CommandType;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PublishFixedHeader;
import com.jbroker.utils.ByteUtils;
import java.io.IOException;
import java.io.InputStream;

public class FixedHeaderReader {

  public FixedHeader read(InputStream input) throws IOException {
    int firstByte = input.read();
    int controlPacketType = readControlPacketType(firstByte);
    int remainingLength = readRemainingLength(input);

    if (controlPacketType == CommandType.PUBLISH.getValue()) {
      return readPublishFixedHeader(controlPacketType, remainingLength, (byte) firstByte);
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
  private static PublishFixedHeader readPublishFixedHeader(
      int controlPacketType,
      int remainingLength,
      byte firstByte) {
    boolean duplicateFlag = readDuplicateFlag(firstByte);
    int qosLevel = readQosLevel(firstByte);
    boolean retain = readRetainFlag(firstByte);
    return new PublishFixedHeader(
        controlPacketType,
        remainingLength,
        duplicateFlag,
        qosLevel,
        retain
    );
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.1
   * DUP</a>
   */
  private static boolean readDuplicateFlag(byte firstByte) {
    return ByteUtils.isBitSet(firstByte, DUPLICATE_FLAG_BIT);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.2
   * QoS</a>
   */
  private static int readQosLevel(byte firstByte) {
    return ByteUtils.combineBits(firstByte, QOS_LEVEL_START_BIT, QOS_LEVEL_END_BIT);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718038">3.3.1.3
   * RETAIN</a>
   */
  private static boolean readRetainFlag(byte firstByte) {
    return ByteUtils.isBitSet(firstByte, RETAIN_FLAG_BIT);
  }
}
