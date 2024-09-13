package com.jbroker.client;

import java.io.IOException;
import java.net.Socket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientConnectionManager {

  private final ClientConnectionFactory clientConnectionFactory;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public ClientConnection createClientConnection(Socket clientSocket) throws IOException {
    ClientConnection clientConnection = clientConnectionFactory.createClientConnection(
        clientSocket
    );
    clientConnectionRegistry.addClientConnection(
        clientSocket.getRemoteSocketAddress(),
        clientConnection
    );
    return clientConnection;
  }
}
