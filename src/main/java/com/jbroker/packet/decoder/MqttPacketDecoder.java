package com.jbroker.packet.decoder;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import java.io.IOException;

public interface MqttPacketDecoder<InboundPacket extends MqttPacket> {

  InboundPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) throws IOException;
}
