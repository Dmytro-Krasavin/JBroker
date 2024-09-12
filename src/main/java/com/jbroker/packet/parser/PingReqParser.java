package com.jbroker.packet.parser;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PingReqPacket;

public class PingReqParser implements MqttPacketParser {

  @Override
  public MqttPacket parse(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new PingReqPacket(fixedHeader);
  }
}
