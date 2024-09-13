package com.jbroker;

import com.jbroker.client.ClientConnectionManager;
import com.jbroker.message.MessageQueue;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Broker {

  private final ClientConnectionManager clientConnectionManager;
  private final MessageQueue messageQueue;

  public void run(int port) {
    messageQueue.start();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("MQTT Broker is listening on port {}", port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        // Handle client communication in a separate thread
        clientConnectionManager.createClientConnection(clientSocket).start();
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    }
  }
}
