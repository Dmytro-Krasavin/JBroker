package com.jbroker.packet.parser;

import com.jbroker.command.CommandType;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import java.io.IOException;
import java.io.InputStream;

public class PacketParserDispatcher {

  private final FixedHeaderParser fixedHeaderParser;
  private final ConnectParser connectParser;
  private final PingReqParser pingReqParser;
  private final DisconnectParser disconnectParser;

  public PacketParserDispatcher(
      FixedHeaderParser fixedHeaderParser,
      ConnectParser connectParser,
      PingReqParser pingReqParser,
      DisconnectParser disconnectParser) {
    this.fixedHeaderParser = fixedHeaderParser;
    this.connectParser = connectParser;
    this.pingReqParser = pingReqParser;
    this.disconnectParser = disconnectParser;
  }

  public MqttPacket parse(InputStream input) throws IOException {
    FixedHeader fixedHeader = fixedHeaderParser.parse(input);
    byte[] packetBuffer = buildPacketBuffer(input, fixedHeader.remainingLength());

    CommandType commandType = CommandType.resolveType(fixedHeader.controlPacketType());
    return switch (commandType) {
      case CONNECT -> connectParser.parse(fixedHeader, packetBuffer);
      case PINGREQ -> pingReqParser.parse(fixedHeader, packetBuffer);
      case DISCONNECT -> disconnectParser.parse(fixedHeader, packetBuffer);
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
