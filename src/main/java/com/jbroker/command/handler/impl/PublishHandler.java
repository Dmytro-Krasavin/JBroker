package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.message.queue.MessageQueue;
import com.jbroker.packet.model.bidirectional.impl.PublishPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PublishHandler implements CommandHandler<PublishPacket, ServerToClientPacket> {

  private final MessageQueue messageQueue;

  @Override
  public Optional<ServerToClientPacket> handleCommand(
      PublishPacket publishPacket,
      String clientId) {
    String topic = publishPacket.getTopicName();
    String applicationMessage = publishPacket.getApplicationMessage();
    log.info("Topic: {}", topic);
    log.info("QoS Level: {}", publishPacket.getFixedHeader().getQosLevel());
    log.info("Retain: {}", publishPacket.getFixedHeader().isRetain());
    log.info("Packet Identifier: {}", publishPacket.getPacketIdentifier());
    log.info("Application Message:\n{}", applicationMessage);

    messageQueue.add(publishPacket);

    return Optional.empty();
  }
}
