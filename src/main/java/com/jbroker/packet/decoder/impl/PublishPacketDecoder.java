package com.jbroker.packet.decoder.impl;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PublishFixedHeader;
import com.jbroker.packet.PublishPacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;

public class PublishPacketDecoder implements MqttPacketDecoder<PublishPacket> {

  @Override
  public PublishPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    if (fixedHeader instanceof PublishFixedHeader publishFixedHeader) {
      // TODO: decode topic, packet identifier, payload (application message)
      return new PublishPacket(publishFixedHeader);
    }
    throw new IllegalStateException(
        "Fixed Header for PUBLISH packet should be instance of PublishFixedHeader");
  }
}
