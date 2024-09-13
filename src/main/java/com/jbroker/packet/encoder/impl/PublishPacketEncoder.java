package com.jbroker.packet.encoder.impl;

import static com.jbroker.utils.PacketEncodeUtils.encodeApplicationMessage;
import static com.jbroker.utils.PacketEncodeUtils.encodePacketIdentifier;
import static com.jbroker.utils.PacketEncodeUtils.encodeTextField;

import com.jbroker.packet.PublishPacket;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import com.jbroker.utils.ArrayUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PublishPacketEncoder implements MqttPacketEncoder<PublishPacket> {

  private final FixedHeaderEncoder fixedHeaderEncoder;

  @Override
  public byte[] encode(PublishPacket outboundPacket) {
    byte[] encodedFixedHeader = fixedHeaderEncoder.encode(outboundPacket.getFixedHeader());
    byte[] encodedTopicName = encodeTextField(outboundPacket.getTopicName());
    byte[] encodedPacketIdentifier = outboundPacket.getPacketIdentifier() != null
        ? encodePacketIdentifier(outboundPacket.getPacketIdentifier())
        : new byte[0];
    byte[] encodedPayload = encodeApplicationMessage(outboundPacket.getApplicationMessage());
    return ArrayUtils.mergeArrays(
        encodedFixedHeader,
        encodedTopicName,
        encodedPacketIdentifier,
        encodedPayload
    );
  }
}
