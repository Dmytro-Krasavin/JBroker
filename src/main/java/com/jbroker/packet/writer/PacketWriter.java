package com.jbroker.packet.writer;

import com.jbroker.command.CommandType;
import com.jbroker.exception.PacketSendFailedException;
import com.jbroker.packet.ConnackPacket;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PingRespPacket;
import com.jbroker.packet.SubackPacket;
import com.jbroker.packet.encoder.impl.ConnackPacketEncoder;
import com.jbroker.packet.encoder.impl.PingRespPacketEncoder;
import com.jbroker.packet.encoder.impl.SubackPacketEncoder;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketWriter {

  private final ConnackPacketEncoder connackEncoder;
  private final PingRespPacketEncoder pingRespEncoder;
  private final SubackPacketEncoder subackEncoder;

  public void write(OutputStream output, MqttPacket outboundPacket) {
    CommandType commandType = outboundPacket.getFixedHeader().getCommandType();
    byte[] encodedPacket = switch (commandType) {
      case CONNACK -> connackEncoder.encode((ConnackPacket) outboundPacket);
      case PINGRESP -> pingRespEncoder.encode((PingRespPacket) outboundPacket);
      case SUBACK -> subackEncoder.encode((SubackPacket) outboundPacket);
      default -> throw new IllegalArgumentException(
          "Could not find applicable packet decoder for command type: " + commandType.name());
    };
    sendPacket(output, encodedPacket, commandType);
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
