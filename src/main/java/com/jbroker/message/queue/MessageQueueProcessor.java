package com.jbroker.message.queue;

import com.jbroker.message.MessagePublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageQueueProcessor implements Runnable {

  private final MessageQueue messageQueue;
  private final MessagePublisher messagePublisher;

  @Override
  public void run() {
    while (true) {
      publishQueuedMessages();
    }
  }

  private void publishQueuedMessages() {
    while (messageQueue.hasMessages()) {
      messagePublisher.publish(messageQueue.poll());
    }
  }
}
