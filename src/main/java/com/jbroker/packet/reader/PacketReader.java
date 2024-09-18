package com.jbroker.packet.reader;

import com.jbroker.command.CommandType;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import com.jbroker.packet.decoder.PacketDecoderFactory;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.ClientToServerPacket;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketReader {

  private final FixedHeaderReader fixedHeaderReader;
  private final PacketDecoderFactory packetDecoderFactory;

  public ClientToServerPacket read(int firstByte, InputStream inputStream) throws IOException {
    FixedHeader fixedHeader = fixedHeaderReader.read(firstByte, inputStream);
    byte[] packetBuffer = buildPacketBuffer(inputStream, fixedHeader.getRemainingLength());

    CommandType commandType = fixedHeader.getCommandType();
    log.info("{} packet received from client", commandType.name());

    MqttPacketDecoder packetDecoder = packetDecoderFactory.getPacketDecoder(commandType);
    return packetDecoder.decode(fixedHeader, packetBuffer);
  }

  private byte[] buildPacketBuffer(InputStream inputStream, int remainingLength)
      throws IOException {
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
