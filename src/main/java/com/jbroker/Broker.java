package com.jbroker;

import com.jbroker.client.ClientConnectionManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Broker {

  private final ClientConnectionManager clientConnectionManager;

  public void run(int port) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("MQTT Broker is listening on port {}", port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        // Handle client communication in a separate thread
        clientConnectionManager.startClientConnection(clientSocket);
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    }
  }
}
