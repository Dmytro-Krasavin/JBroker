package com.jbroker.packet.decoder.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.impl.PingReqPacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;

public class PingReqPacketDecoder implements MqttPacketDecoder {

  @Override
  public PingReqPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new PingReqPacket(fixedHeader);
  }
}
