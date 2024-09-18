package com.jbroker.message;

import com.jbroker.connection.registry.ClientConnectionRegistry;
import com.jbroker.packet.model.bidirectional.impl.PublishPacket;
import com.jbroker.subscription.registry.SubscriptionRegistry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessagePublisher {

  private final SubscriptionRegistry subscriptionRegistry;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public void publish(PublishPacket publishPacket) {
    subscriptionRegistry.getSubscribers(publishPacket.getTopicName())
        .forEach(subscriber ->
            clientConnectionRegistry.sendPacket(subscriber.clientId(), publishPacket)
        );
  }
}
