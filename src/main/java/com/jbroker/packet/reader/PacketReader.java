package com.jbroker.packet.reader;

import com.jbroker.command.CommandType;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.PublishPacketDecoder;
import com.jbroker.packet.decoder.impl.SubscribePacketDecoder;
import com.jbroker.packet.decoder.impl.UnsubscribePacketDecoder;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketReader {

  private final FixedHeaderReader fixedHeaderReader;
  private final ConnectPacketDecoder connectDecoder;
  private final PingReqPacketDecoder pingReqDecoder;
  private final PublishPacketDecoder publishDecoder;
  private final SubscribePacketDecoder subscribeDecoder;
  private final UnsubscribePacketDecoder unsubscribeDecoder;
  private final DisconnectPacketDecoder disconnectDecoder;

  public MqttPacket read(int firstByte, InputStream inputStream) throws IOException {
    FixedHeader fixedHeader = fixedHeaderReader.read(firstByte, inputStream);
    byte[] packetBuffer = buildPacketBuffer(inputStream, fixedHeader.getRemainingLength());

    CommandType commandType = fixedHeader.getCommandType();
    log.info("{} packet received from client", commandType.name());

    return switch (commandType) {
      case CONNECT -> connectDecoder.decode(fixedHeader, packetBuffer);
      case PINGREQ -> pingReqDecoder.decode(fixedHeader, packetBuffer);
      case PUBLISH -> publishDecoder.decode(fixedHeader, packetBuffer);
      case SUBSCRIBE -> subscribeDecoder.decode(fixedHeader, packetBuffer);
      case UNSUBSCRIBE -> unsubscribeDecoder.decode(fixedHeader, packetBuffer);
      case DISCONNECT -> disconnectDecoder.decode(fixedHeader, packetBuffer);
      default -> throw new IllegalArgumentException(
          "Could not find applicable packet encoder for command type: " + commandType.name());
    };
  }

  private byte[] buildPacketBuffer(InputStream inputStream, int remainingLength) throws IOException {
    byte[] packetBuffer = new byte[remainingLength];
    int totalReadBytes = inputStream.read(packetBuffer);
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
