package com.jbroker.message.queue.impl;

import com.jbroker.message.queue.MessageQueue;
import com.jbroker.packet.PublishPacket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryMessageQueue implements MessageQueue {

  private final BlockingQueue<PublishPacket> messages = new LinkedBlockingQueue<>();

  public void add(PublishPacket publishPacket) {
    messages.add(publishPacket);
  }

  @Override
  public PublishPacket poll() {
    return messages.poll();
  }

  @Override
  public boolean hasMessages() {
    return !messages.isEmpty();
  }
}
