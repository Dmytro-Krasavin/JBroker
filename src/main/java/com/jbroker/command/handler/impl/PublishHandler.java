package com.jbroker.command.handler.impl;

import com.jbroker.client.ClientConnectionRegistry;
import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PublishPacket;
import com.jbroker.subscription.Subscriber;
import com.jbroker.subscription.SubscriptionRegistry;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PublishHandler implements CommandHandler<PublishPacket, MqttPacket> {

  private final SubscriptionRegistry subscriptionRegistry;
  private final ClientConnectionRegistry clientConnectionRegistry;

  @Override
  public Optional<MqttPacket> handleCommand(PublishPacket publishPacket, String clientId) {
    String topic = publishPacket.getTopicName();
    String applicationMessage = publishPacket.getApplicationMessage();
    log.info("Topic: {}", topic);
    log.info("QoS Level: {}", publishPacket.getFixedHeader().getQosLevel());
    log.info("Retain: {}", publishPacket.getFixedHeader().isRetain());
    log.info("Packet Identifier: {}", publishPacket.getPacketIdentifier());
    log.info("Application Message:\n{}", applicationMessage);

    List<Subscriber> subscribers = subscriptionRegistry.getSubscribersForTopic(topic);
    for (Subscriber subscriber : subscribers) {
      clientConnectionRegistry.sendPacket(subscriber.clientId(), publishPacket);
    }

    return Optional.empty();
  }
}
