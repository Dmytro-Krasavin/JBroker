package com.jbroker.message.queue;

import com.jbroker.packet.model.bidirectional.impl.PublishPacket;

public interface MessageQueue {

  void add(PublishPacket publishPacket);

  PublishPacket poll();

  boolean hasMessages();
}
