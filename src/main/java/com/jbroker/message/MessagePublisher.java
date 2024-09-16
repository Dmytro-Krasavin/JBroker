package com.jbroker.message;

import com.jbroker.client.ClientConnectionRegistry;
import com.jbroker.packet.PublishPacket;
import com.jbroker.subscription.SubscriptionRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessagePublisher {

  private final SubscriptionRegistry subscriptionRegistry;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public void publish(PublishPacket publishPacket) {
    subscriptionRegistry.getSubscribersForTopic(publishPacket.getTopicName())
        .forEach(subscriber ->
            clientConnectionRegistry.sendPacket(subscriber.clientId(), publishPacket)
        );
  }
}
