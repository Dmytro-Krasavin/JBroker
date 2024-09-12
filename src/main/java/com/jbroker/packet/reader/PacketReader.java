package com.jbroker.packet.reader;

import com.jbroker.command.CommandType;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import java.io.IOException;
import java.io.InputStream;

public class PacketReader {

  private final FixedHeaderReader fixedHeaderReader;
  private final ConnectPacketDecoder connectDecoder;
  private final PingReqPacketDecoder pingReqDecoder;
  private final DisconnectPacketDecoder disconnectDecoder;

  public PacketReader(
      FixedHeaderReader fixedHeaderReader,
      ConnectPacketDecoder connectDecoder,
      PingReqPacketDecoder pingReqDecoder,
      DisconnectPacketDecoder disconnectDecoder) {
    this.fixedHeaderReader = fixedHeaderReader;
    this.connectDecoder = connectDecoder;
    this.pingReqDecoder = pingReqDecoder;
    this.disconnectDecoder = disconnectDecoder;
  }

  public MqttPacket read(InputStream input) throws IOException {
    FixedHeader fixedHeader = fixedHeaderReader.read(input);
    byte[] packetBuffer = buildPacketBuffer(input, fixedHeader.remainingLength());

    CommandType commandType = CommandType.resolveType(fixedHeader.controlPacketType());
    return switch (commandType) {
      case CONNECT -> connectDecoder.decode(fixedHeader, packetBuffer);
      case PINGREQ -> pingReqDecoder.decode(fixedHeader, packetBuffer);
      case DISCONNECT -> disconnectDecoder.decode(fixedHeader, packetBuffer);
      default -> throw new IllegalArgumentException(
          "Could not find applicable packet parser for command type: " + commandType.name());
    };
  }

  private byte[] buildPacketBuffer(InputStream input, int remainingLength) throws IOException {
    byte[] packetBuffer = new byte[remainingLength];
    int totalReadBytes = input.read(packetBuffer);
    if (totalReadBytes < remainingLength) {
      throw new IllegalStateException(String.format(
          "Not all bytes were read to fill the packet buffer. Total read bytes: %s, remaining length: %s",
          totalReadBytes,
          remainingLength
      ));
    }
    return packetBuffer;
  }
}
