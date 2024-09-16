package com.jbroker.packet.encoder.impl;

import static com.jbroker.utils.PacketEncodeUtils.encodePacketIdentifier;

import com.jbroker.packet.model.outbound.impl.UnsubackPacket;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import com.jbroker.utils.ArrayUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnsubackPacketEncoder implements MqttPacketEncoder<UnsubackPacket> {

  private final FixedHeaderEncoder fixedHeaderEncoder;

  @Override
  public byte[] encode(UnsubackPacket outboundPacket) {
    byte[] encodedFixedHeader = fixedHeaderEncoder.encode(outboundPacket.getFixedHeader());
    byte[] encodedVariableHeader = encodePacketIdentifier(outboundPacket.getPacketIdentifier());
    return ArrayUtils.mergeArrays(encodedFixedHeader, encodedVariableHeader);
  }
}
