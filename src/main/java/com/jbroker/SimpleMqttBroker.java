package com.jbroker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleMqttBroker {

  private static final int PORT = 1884; // Standard MQTT port

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("MQTT Broker is listening on port " + PORT);

      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("New client connected: " + socket.toString());

        // Handle client communication in a separate thread
        new ClientHandler(socket).start();
      }
    } catch (IOException e) {
      System.err.println("IOException occurred: " + e.getMessage());
    }
  }
}
