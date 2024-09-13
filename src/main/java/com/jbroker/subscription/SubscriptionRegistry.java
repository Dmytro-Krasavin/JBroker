package com.jbroker.subscription;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SubscriptionRegistry {

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

  public List<Subscriber> getSubscribersForTopic(String topic) {
    List<Subscriber> matchingSubscribers = new ArrayList<>();
    subscribersByTopic.forEach((subscribedTopic, subscribers) -> {
      if (isTopicMatching(subscribedTopic, topic)) {
        matchingSubscribers.addAll(subscribers);
      }
    });
    return matchingSubscribers;
  }

  private boolean isTopicMatching(String subscribedTopic, String publishTopic) {
    String[] subscribedLevels = subscribedTopic.split("/");
    String[] publishLevels = publishTopic.split("/");
    for (int i = 0; i < subscribedLevels.length; i++) {
      if (subscribedLevels[i].equals("#")) {
        return true; // multi-level wildcard '#' matches anything
      }

      if (!subscribedLevels[i].equals("+") && !subscribedLevels[i].equals(publishLevels[i])) {
        return false; // mismatch
      }
    }
    return subscribedLevels.length == publishLevels.length; // Exact match
  }
}
