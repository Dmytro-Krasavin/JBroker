package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.AbstractCommandHandler;
import com.jbroker.message.queue.MessageQueue;
import com.jbroker.packet.model.bidirectional.impl.PublishPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PublishHandler extends AbstractCommandHandler<PublishPacket, ServerToClientPacket> {

  private final MessageQueue messageQueue;

  @Override
  protected ServerToClientPacket getOutboundPacket(PublishPacket publishPacket) {
    String topic = publishPacket.getTopicName();
    String applicationMessage = publishPacket.getApplicationMessage();
    log.info("Topic: {}, QoS Level: {}, Retain: {}",
        topic,
        publishPacket.getFixedHeader().getQosLevel(),
        publishPacket.getFixedHeader().isRetain()
    );
    log.debug("Packet Identifier: {}", publishPacket.getPacketIdentifier());
    log.debug("Application Message:\n{}", applicationMessage);

    return super.getOutboundPacket(publishPacket);
  }

  @Override
  protected void doSideEffects(PublishPacket publishPacket, String clientId) {
    messageQueue.add(publishPacket);
  }
}
