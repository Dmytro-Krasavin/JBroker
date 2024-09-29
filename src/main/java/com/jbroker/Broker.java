package com.jbroker;

import com.jbroker.connection.ClientConnection;
import com.jbroker.connection.ClientConnectionManager;
import com.jbroker.message.queue.MessageQueueProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Broker {

  private final ClientConnectionManager clientConnectionManager;
  private final MessageQueueProcessor messageQueueProcessor;
  private final ExecutorService connectionExecutor;
  private final ExecutorService messageProcessingExecutor;

  public Broker(ClientConnectionManager clientConnectionManager,
      MessageQueueProcessor messageQueueProcessor) {
    this.clientConnectionManager = clientConnectionManager;
    this.messageQueueProcessor = messageQueueProcessor;
    this.connectionExecutor = Executors.newCachedThreadPool();
    this.messageProcessingExecutor = Executors.newSingleThreadExecutor();
  }

  /**
   * Starts the MQTT broker, listening on the provided port for incoming client connections.
   */
  public void run(int port) {
    messageProcessingExecutor.submit(messageQueueProcessor);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("MQTT Broker is listening on port {}", port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        ClientConnection connection = clientConnectionManager.createClientConnection(clientSocket);
        connectionExecutor.submit(connection);
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    }
  }
}
