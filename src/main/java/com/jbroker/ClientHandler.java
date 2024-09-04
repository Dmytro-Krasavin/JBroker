package com.jbroker;

import com.jbroker.logger.Logger;
import com.jbroker.packet.MqttPacket;
import com.jbroker.parser.PacketParser;
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
  private final PacketParser packetParser;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.inetAddress = socket.getInetAddress();
    this.port = socket.getPort();
    this.log = Logger.getInstance();
    this.packetParser = new PacketParser();
  }

  @Override
  public void run() {
    try (InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream()) {

      // Main loop to handle incoming packets
      while (true) {
        if (input.available() == 0) {
          // End of stream reached
          continue;
        }

        MqttPacket packet = packetParser.parse(input);
        int controlPacketType = packet.fixedHeader().controlPacketType();
        int remainingLength = packet.fixedHeader().remainingLength();
        switch (controlPacketType) {
          case 1: // CONNECT packet
            handleConnect(input, output, remainingLength);
            break;
          case 12: // PINGREQ packet
            handlePingreq(output, remainingLength);
            break;
          case 14: // DISCONNECT packet
            handleDisconnect();
            return; // Exit the loop and close the socket
          // Add more cases as you implement other packet types
          default:
            log.error("Unknown or unsupported control packet type: %s", controlPacketType);
            break;
        }
        log.info("Remaining length: %s", remainingLength);
      }
    } catch (IOException e) {
      log.error("IOException occurred: %s", e.getMessage());
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        System.err.println("IOException occurred while closing socket: " + e.getMessage());
      }
    }
  }

  private void handleConnect(InputStream input, OutputStream output, int remainingLength)
      throws IOException {
    // Skip remaining length byte(s)
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

  private void handlePingreq(OutputStream output, int remainingLength)
      throws IOException {
    // PINGREQ is a fixed two-byte packet
    if (remainingLength != 0x00) { // Should be 0x00 for PINGREQ
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
