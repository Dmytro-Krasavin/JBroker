package com.jbroker.packet.decoder.impl;

import com.jbroker.packet.model.inbound.impl.DisconnectPacket;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.decoder.MqttPacketDecoder;

public class DisconnectPacketDecoder implements MqttPacketDecoder {

  @Override
  public DisconnectPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new DisconnectPacket(fixedHeader);
  }
}
