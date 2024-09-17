package com.jbroker.message.topic;

public class TopicFilter {

  public boolean isTopicMatching(String subscribedTopic, String publishTopic) {
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
