package com.jbroker.packet.decoder;

import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.header.FixedHeader;

public interface MqttPacketDecoder {

  ClientToServerPacket decode(FixedHeader fixedHeader, byte[] packetBuffer);
}
