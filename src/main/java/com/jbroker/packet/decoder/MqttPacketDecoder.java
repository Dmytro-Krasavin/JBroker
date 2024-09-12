package com.jbroker.packet.decoder;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import java.io.IOException;

public interface MqttPacketDecoder {

  MqttPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) throws IOException;
}
