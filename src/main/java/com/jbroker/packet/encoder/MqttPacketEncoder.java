package com.jbroker.packet.encoder;

import com.jbroker.packet.model.outbound.ServerToClientPacket;

public interface MqttPacketEncoder<Out extends ServerToClientPacket> {

  byte[] encode(Out outboundPacket);
}
