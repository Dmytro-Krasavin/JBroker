package com.jbroker.packet.decoder.impl;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PingReqPacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;

public class PingReqPacketDecoder implements MqttPacketDecoder {

  @Override
  public MqttPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new PingReqPacket(fixedHeader);
  }
}
