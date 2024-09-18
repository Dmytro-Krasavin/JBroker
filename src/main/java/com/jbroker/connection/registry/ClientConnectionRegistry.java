package com.jbroker.connection.registry;

import com.jbroker.connection.ClientConnection;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.net.SocketAddress;

public interface ClientConnectionRegistry {

  void addClientConnection(SocketAddress socketAddress, ClientConnection clientConnection);

  void removeClientConnection(String clientId);

  void sendPacket(String clientId, ServerToClientPacket outboundPacket);
}
