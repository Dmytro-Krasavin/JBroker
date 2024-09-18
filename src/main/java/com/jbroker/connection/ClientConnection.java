package com.jbroker.connection;

import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.net.SocketAddress;

public interface ClientConnection extends Runnable {

  void sentPacket(ServerToClientPacket outboundPacket);

  String getMqttClientId();

  SocketAddress getSocketAddress();
}
