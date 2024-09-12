package com.jbroker.packet.encoder;

import com.jbroker.packet.MqttPacket;
import java.io.IOException;

public interface MqttPacketEncoder<OutboundPacket extends MqttPacket> {

  byte[] encode(OutboundPacket outboundPacket) throws IOException;
}
