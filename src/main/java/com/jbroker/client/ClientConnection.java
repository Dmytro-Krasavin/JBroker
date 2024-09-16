package com.jbroker.client;

import com.jbroker.packet.MqttPacket;

public interface ClientConnection extends Runnable {

  void sentPacket(MqttPacket outboundPacket);

  String getClientId();
}
