package com.jbroker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler extends Thread {

  private final Socket socket;
  private final InetAddress inetAddress;
  private final int port;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.inetAddress = socket.getInetAddress();
    this.port = socket.getPort();
  }

  @Override
  public void run() {
    try (InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream()) {

      // Main loop to handle incoming packets
      while (true) {
        int packetType = input.read();
        if (packetType == -1) {
          // End of stream reached, client disconnected
          break;
        }

        switch (packetType) {
          case 0x10: // CONNECT packet
            handleConnect(input, output);
            break;
          // Add more cases as you implement other packet types
          default:
            System.out.println("Unknown or unsupported packet type: " + packetType);
            break;
        }
      }
    } catch (IOException e) {
      System.err.println("IOException occurred: " + e.getMessage());
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.println("IOException occurred while closing socket: " + e.getMessage());
      }
    }
  }

  private void handleConnect(InputStream input, OutputStream output) throws IOException {
    // Skip remaining length byte(s)
    int remainingLength = input.read();

    // Parse the CONNECT packet according to the MQTT protocol
    byte[] connectPacket = new byte[remainingLength];
    input.read(connectPacket);

    // Construct a simple CONNACK packet
    byte[] connackPacket = {
        (byte) 0x20, // CONNACK packet type
        (byte) 0x02, // Remaining length
        (byte) 0x00, // Connect Acknowledge Flags
        (byte) 0x00  // Connect Return Code (0x00 = Connection Accepted)
    };

    // Send the CONNACK packet back to the client
    output.write(connackPacket);
    output.flush();

    log("CONNACK packet sent to client");
  }

  private void log(String message) {
    System.out.printf("%s:%s : %s%n", inetAddress.toString(), port, message);
  }
}
