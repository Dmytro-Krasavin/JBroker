package com.jbroker.client;

import com.jbroker.packet.PublishPacket;
import com.jbroker.subscription.SubscriptionRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientPublisher {

  private final SubscriptionRegistry subscriptionRegistry;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public void publishMessage(PublishPacket publishPacket) {
    subscriptionRegistry.getSubscribersForTopic(publishPacket.getTopicName())
        .forEach(subscriber ->
            clientConnectionRegistry.sendPacket(subscriber.clientId(), publishPacket)
        );
  }
}
