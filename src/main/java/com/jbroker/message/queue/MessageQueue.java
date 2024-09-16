package com.jbroker.message.queue;

import com.jbroker.packet.PublishPacket;

public interface MessageQueue {

  void add(PublishPacket publishPacket);

  PublishPacket poll();

  boolean hasMessages();
}
