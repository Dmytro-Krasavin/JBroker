package com.jbroker.packet.encoder.impl;

import com.jbroker.packet.PingRespPacket;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PingRespPacketEncoder implements MqttPacketEncoder<PingRespPacket> {

  private final FixedHeaderEncoder fixedHeaderEncoder;

  @Override
  public byte[] encode(PingRespPacket outboundPacket) {
    return fixedHeaderEncoder.encode(outboundPacket.getFixedHeader());
  }
}
