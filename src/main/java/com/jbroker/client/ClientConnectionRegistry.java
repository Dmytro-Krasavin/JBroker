package com.jbroker.client;

import com.jbroker.packet.MqttPacket;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientConnectionRegistry {

  private final ConcurrentMap<SocketAddress, ClientConnection> clientConnectionBySocketAddressMap
      = new ConcurrentHashMap<>();

  public void addClientConnection(SocketAddress socketAddress, ClientConnection clientConnection) {
    clientConnectionBySocketAddressMap.put(socketAddress, clientConnection);
  }

  public void sendPacket(String clientId, MqttPacket outboundPacket) {
    clientConnectionBySocketAddressMap.values().stream()
        .filter(clientConnection -> Objects.nonNull(clientConnection.getClientId()))
        .filter(clientConnection -> clientId.equals(clientConnection.getClientId()))
        .forEach(clientConnection -> clientConnection.sentPacket(outboundPacket));
  }
}
