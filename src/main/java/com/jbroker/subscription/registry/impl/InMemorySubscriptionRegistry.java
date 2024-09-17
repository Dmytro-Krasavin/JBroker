package com.jbroker.subscription.registry.impl;


import com.jbroker.message.topic.TopicFilter;
import com.jbroker.subscription.Subscriber;
import com.jbroker.subscription.registry.SubscriptionRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemorySubscriptionRegistry implements SubscriptionRegistry {

  private final TopicFilter topicFilter;
  private final ConcurrentMap<String, Set<Subscriber>> subscribersByTopic = new ConcurrentHashMap<>();

  public void subscribe(String topic, Subscriber subscriber) {
    subscribersByTopic.computeIfAbsent(topic, key -> new HashSet<>()).add(subscriber);
  }

  public void unsubscribe(String topic, String clientId) {
    if (subscribersByTopic.containsKey(topic)) {
      Set<Subscriber> subscribers = subscribersByTopic.get(topic);
      subscribers.removeIf(subscriberClient -> subscriberClient.clientId().equals(clientId));

      if (subscribers.isEmpty()) {
        subscribersByTopic.remove(topic);
      }
    }
  }

  public List<Subscriber> getSubscribers(String topic) {
    List<Subscriber> matchingSubscribers = new ArrayList<>();
    subscribersByTopic.forEach((subscribedTopic, subscribers) -> {
      if (topicFilter.isTopicMatching(subscribedTopic, topic)) {
        matchingSubscribers.addAll(subscribers);
      }
    });
    return matchingSubscribers;
  }
}
