package com.jbroker.packet.parser;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import java.io.IOException;

public interface MqttPacketParser {

  MqttPacket parse(FixedHeader fixedHeader, byte[] packetBuffer) throws IOException;
}
