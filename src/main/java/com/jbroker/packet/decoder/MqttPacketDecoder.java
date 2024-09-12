package com.jbroker.packet.decoder;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;

public interface MqttPacketDecoder<InboundPacket extends MqttPacket> {

  InboundPacket decode(FixedHeader fixedHeader, byte[] packetBuffer);
}
