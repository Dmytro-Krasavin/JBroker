package com.jbroker.packet.parser;

import com.jbroker.packet.DisconnectPacket;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;

public class DisconnectParser implements MqttPacketParser {

  @Override
  public MqttPacket parse(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new DisconnectPacket(fixedHeader);
  }
}
