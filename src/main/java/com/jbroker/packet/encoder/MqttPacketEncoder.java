package com.jbroker.packet.encoder;

import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.io.IOException;

public interface MqttPacketEncoder<Out extends ServerToClientPacket> {

  byte[] encode(Out outboundPacket) throws IOException;
}
