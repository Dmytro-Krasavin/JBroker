package com.jbroker.packet.writer;

import com.jbroker.command.CommandType;
import com.jbroker.exception.PacketSendFailedException;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import com.jbroker.packet.encoder.PacketEncoderFactory;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketWriter {

  private final PacketEncoderFactory packetEncoderFactory;

  @SuppressWarnings("unchecked")
  public void write(OutputStream outputStream, ServerToClientPacket outboundPacket) {
    CommandType commandType = outboundPacket.getCommandType();
    MqttPacketEncoder<ServerToClientPacket> packetEncoder = packetEncoderFactory.getPacketEncoder(
        commandType);
    byte[] encodedPacket = packetEncoder.encode(outboundPacket);
    sendPacket(outputStream, encodedPacket, commandType);

    log.info("{} packet sent to client", commandType.name());
  }

  private void sendPacket(OutputStream output, byte[] encodedPacket, CommandType commandType) {
    try {
      output.write(encodedPacket);
      output.flush();
    } catch (IOException e) {
      throw new PacketSendFailedException(commandType, e);
    }
  }
}
