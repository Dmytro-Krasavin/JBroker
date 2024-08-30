package com.jbroker;

import com.jbroker.logger.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler extends Thread {

  private final Socket socket;
  private final InetAddress inetAddress;
  private final int port;
  private final Logger log;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.inetAddress = socket.getInetAddress();
    this.port = socket.getPort();
    this.log = Logger.getInstance();
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
          case 0xC0: // PINGREQ packet
            handlePingreq(input, output);
            break;
          case 0xE0: // DISCONNECT packet
            handleDisconnect();
            return; // Exit the loop and close the socket
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

    log.info("%s:%s : CONNACK packet sent to client", inetAddress.toString(), port);
  }

  private void handlePingreq(InputStream input, OutputStream output) throws IOException {
    // PINGREQ is a fixed two-byte packet
    int remainingLength = input.read(); // Should be 0x00 for PINGREQ
    if (remainingLength != 0x00) {
      log.error("%s:%s : Invalid PINGREQ packet received", inetAddress.toString(), port);
      return;
    }

    // Construct the PINGRESP packet
    byte[] pingrespPacket = {
        (byte) 0xD0, // PINGRESP packet type
        (byte) 0x00  // Remaining length
    };

    // Send the PINGRESP packet back to the client
    output.write(pingrespPacket);
    output.flush();

    log.info("%s:%s : PINGRESP packet sent to client", inetAddress.toString(), port);
  }

  private void handleDisconnect() {
    log.info(
        "%s:%s : Received DISCONNECT packet, closing connection",
        inetAddress.toString(),
        port
    );
  }
}
