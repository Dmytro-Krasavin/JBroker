package com.jbroker.packet.decoder.impl;

import com.jbroker.packet.DisconnectPacket;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.decoder.MqttPacketDecoder;

public class DisconnectPacketDecoder implements MqttPacketDecoder<DisconnectPacket> {

  @Override
  public DisconnectPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    return new DisconnectPacket(fixedHeader);
  }
}
