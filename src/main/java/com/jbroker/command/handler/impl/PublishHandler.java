package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PublishPacket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishHandler implements CommandHandler<PublishPacket, MqttPacket> {

  @Override
  public Optional<MqttPacket> handleCommand(PublishPacket publishPacket) {
    log.info("Topic: {}", publishPacket.getTopicName());
    log.info("QoS Level: {}", publishPacket.getFixedHeader().getQosLevel());
    log.info("Retain: {}", publishPacket.getFixedHeader().isRetain());
    log.info("Packet Identifier: {}", publishPacket.getPacketIdentifier());
    log.info("Application Message:\n{}", publishPacket.getApplicationMessage());
    return Optional.empty();
  }
}
