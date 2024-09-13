package com.jbroker.message;

import com.jbroker.client.ClientPublisher;
import com.jbroker.packet.PublishPacket;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageQueue extends Thread {

  private final ClientPublisher clientPublisher;
  private final ConcurrentLinkedQueue<PublishPacket> messages = new ConcurrentLinkedQueue<>();

  public void add(PublishPacket publishPacket) {
    messages.add(publishPacket);
  }

  @Override
  public void run() {
    while (true) {
      while (!messages.isEmpty()) {
        clientPublisher.publishMessage(messages.poll());
      }
    }
  }
}
