package com.jbroker.packet.reader;

import com.jbroker.packet.FixedHeader;
import java.io.IOException;
import java.io.InputStream;

public class FixedHeaderReader {

  public FixedHeader read(InputStream input) throws IOException {
    int firstByte = input.read();
    int controlPacketType = readControlPacketType(firstByte);
    int remainingLength = readRemainingLength(input);
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
}
