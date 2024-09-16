package com.jbroker.client;

import com.jbroker.packet.model.outbound.ServerToClientPacket;

public interface ClientConnection extends Runnable {

  void sentPacket(ServerToClientPacket outboundPacket);

  String getClientId();
}
