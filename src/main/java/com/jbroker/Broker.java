package com.jbroker;

import com.jbroker.client.ClientHandlerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Broker {

  private final ClientHandlerFactory clientHandlerFactory;

  public Broker(ClientHandlerFactory clientHandlerFactory) {
    this.clientHandlerFactory = clientHandlerFactory;
  }

  public void run(int port) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("MQTT Broker is listening on port " + port);

      while (true) {
        Socket socket = serverSocket.accept();
        // Handle client communication in a separate thread
        clientHandlerFactory.createClientHandler(socket).start();
      }
    } catch (IOException e) {
      System.err.println("IOException occurred: " + e.getMessage());
    }
  }
}
