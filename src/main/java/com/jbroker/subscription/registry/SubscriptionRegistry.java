package com.jbroker.subscription.registry;


import com.jbroker.subscription.Subscriber;
import java.util.List;

public interface SubscriptionRegistry {

  void subscribe(String topic, Subscriber subscriber);

  void unsubscribe(String topic, String clientId);

  List<Subscriber> getSubscribers(String topic);
}
