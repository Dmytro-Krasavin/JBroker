package com.jbroker.connection.registry.impl;

import com.jbroker.connection.ClientConnection;
import com.jbroker.connection.registry.ClientConnectionRegistry;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.net.SocketAddress;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryClientConnectionRegistry implements ClientConnectionRegistry {

  private final ConcurrentMap<SocketAddress, ClientConnection> clientConnectionBySocketAddressMap
      = new ConcurrentHashMap<>();

  public void addClientConnection(SocketAddress socketAddress, ClientConnection clientConnection) {
    clientConnectionBySocketAddressMap.put(socketAddress, clientConnection);
  }

  public void removeClientConnection(String clientId) {
    findClientConnectionByClientId(clientId)
        .map(ClientConnection::getSocketAddress)
        .ifPresent(clientConnectionBySocketAddressMap::remove);
  }

  public void sendPacket(String clientId, ServerToClientPacket outboundPacket) {
    findClientConnectionByClientId(clientId)
        .ifPresent(clientConnection -> clientConnection.sentPacket(outboundPacket));
  }

  private Optional<ClientConnection> findClientConnectionByClientId(String clientId) {
    return clientConnectionBySocketAddressMap.values().stream()
        .filter(clientConnection -> clientId.equals(clientConnection.getMqttClientId()))
        .findFirst();
  }
}
