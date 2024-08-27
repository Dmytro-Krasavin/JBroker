package com.example.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTcpServer {

  private static final int PORT = 12345; // Port number to listen on

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Server is listening on port " + PORT);

      // Infinite loop to accept client connections
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
