package com.jbroker.packet.encoder;

import static com.jbroker.packet.FixedHeader.CONTROL_PACKET_TYPE_POSITION;
import static com.jbroker.packet.FixedHeader.REMAINING_LENGTH_START_POSITION;
import static com.jbroker.utils.ByteUtils.toArrayIndex;

import com.jbroker.command.CommandType;
import java.util.ArrayList;
import java.util.List;

public class FixedHeaderEncoder {

  public byte[] encode(CommandType commandType, int remainingLength) {
    byte controlPacketType = encodeControlPacketType(commandType);
    List<Byte> remainingLengthBytes = encodeRemainingLength(remainingLength);

    List<Byte> fixedHeaderBytes = new ArrayList<>();
    fixedHeaderBytes.add(toArrayIndex(CONTROL_PACKET_TYPE_POSITION), controlPacketType);
    fixedHeaderBytes.addAll(toArrayIndex(REMAINING_LENGTH_START_POSITION), remainingLengthBytes);

    byte[] encodedFixedHeader = new byte[fixedHeaderBytes.size()];
    for (int i = 0; i < fixedHeaderBytes.size(); i++) {
      encodedFixedHeader[i] = fixedHeaderBytes.get(i);
    }
    return encodedFixedHeader;
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
}
