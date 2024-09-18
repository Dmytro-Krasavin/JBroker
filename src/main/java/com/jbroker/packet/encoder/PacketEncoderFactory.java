package com.jbroker.packet.encoder;

import static com.jbroker.command.CommandType.CONNACK;
import static com.jbroker.command.CommandType.PINGRESP;
import static com.jbroker.command.CommandType.PUBLISH;
import static com.jbroker.command.CommandType.SUBACK;
import static com.jbroker.command.CommandType.UNSUBACK;

import com.jbroker.command.CommandType;
import com.jbroker.packet.encoder.impl.ConnackPacketEncoder;
import com.jbroker.packet.encoder.impl.PingRespPacketEncoder;
import com.jbroker.packet.encoder.impl.PublishPacketEncoder;
import com.jbroker.packet.encoder.impl.SubackPacketEncoder;
import com.jbroker.packet.encoder.impl.UnsubackPacketEncoder;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.EnumMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketEncoderFactory {

  private final Map<CommandType, MqttPacketEncoder<? extends ServerToClientPacket>> packetEncoderByCommandType;

  private final ConnackPacketEncoder connackEncoder;
  private final PingRespPacketEncoder pingRespEncoder;
  private final SubackPacketEncoder subackEncoder;
  private final UnsubackPacketEncoder unsubackEncoder;
  private final PublishPacketEncoder publishEncoder;

  public PacketEncoderFactory(
      ConnackPacketEncoder connackEncoder,
      PingRespPacketEncoder pingRespEncoder,
      SubackPacketEncoder subackEncoder,
      UnsubackPacketEncoder unsubackEncoder,
      PublishPacketEncoder publishEncoder) {
    this.connackEncoder = connackEncoder;
    this.pingRespEncoder = pingRespEncoder;
    this.subackEncoder = subackEncoder;
    this.unsubackEncoder = unsubackEncoder;
    this.publishEncoder = publishEncoder;
    this.packetEncoderByCommandType = new EnumMap<>(CommandType.class);
    initializeMap();
  }

  @SuppressWarnings("unchecked")
  public MqttPacketEncoder<ServerToClientPacket> getPacketEncoder(CommandType commandType) {
    MqttPacketEncoder<ServerToClientPacket> packetEncoder = (MqttPacketEncoder<ServerToClientPacket>) packetEncoderByCommandType.get(
        commandType);
    if (packetEncoder == null) {
      throw new IllegalArgumentException(
          "Could not find applicable packet decoder for command type: " + commandType.name());
    }
    return packetEncoder;
  }

  private void initializeMap() {
    packetEncoderByCommandType.put(CONNACK, connackEncoder);
    packetEncoderByCommandType.put(PINGRESP, pingRespEncoder);
    packetEncoderByCommandType.put(SUBACK, subackEncoder);
    packetEncoderByCommandType.put(UNSUBACK, unsubackEncoder);
    packetEncoderByCommandType.put(PUBLISH, publishEncoder);
  }
}
