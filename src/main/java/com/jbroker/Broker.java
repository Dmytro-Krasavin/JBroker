package com.jbroker;

import com.jbroker.client.ClientConnection;
import com.jbroker.client.ClientConnectionManager;
import com.jbroker.message.queue.MessageQueueProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Broker {

  private final ClientConnectionManager clientConnectionManager;
  private final MessageQueueProcessor messageQueueProcessor;

  public void run(int port) {
    new Thread(messageQueueProcessor).start();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("MQTT Broker is listening on port {}", port);

      while (true) {
        Socket socket = serverSocket.accept();
        // Handle client communication in a separate thread
        ClientConnection connection = clientConnectionManager.createClientConnection(socket);
        new Thread(connection).start();
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    }
  }
}
