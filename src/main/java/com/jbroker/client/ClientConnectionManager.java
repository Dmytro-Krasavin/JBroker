package com.jbroker.client;

import java.io.IOException;
import java.net.Socket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientConnectionManager {

  private final ClientConnectionFactory clientConnectionFactory;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public void startClientConnection(Socket clientSocket) throws IOException {
    ClientConnection clientConnection = clientConnectionFactory.createClientConnection(
        clientSocket
    );
    clientConnection.start();
    clientConnectionRegistry.addClientConnection(
        clientSocket.getRemoteSocketAddress(),
        clientConnection
    );
  }
}
