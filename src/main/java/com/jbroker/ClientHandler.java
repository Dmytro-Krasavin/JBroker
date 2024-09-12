package com.jbroker;

import com.jbroker.command.CommandType;
import com.jbroker.logger.Logger;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.PingReqPacket;
import com.jbroker.packet.reader.PacketReader;
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
  private final PacketReader packetReader;

  public ClientHandler(Socket socket, PacketReader packetReader) {
    this.socket = socket;
    this.inetAddress = socket.getInetAddress();
    this.port = socket.getPort();
    this.log = Logger.getInstance();
    this.packetReader = packetReader;
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

        MqttPacket packet = packetReader.read(input);
        // CommandDispatcher logic
        CommandType commandType = CommandType.resolveType(
            packet.getFixedHeader().controlPacketType()
        );
        switch (commandType) {
          case CONNECT:
            handleConnect(output);
            break;
          case PINGREQ:
            handlePingreq((PingReqPacket) packet, output);
            break;
          case DISCONNECT:
            handleDisconnect();
            return; // Exit the loop and close the socket
          // Add more cases as you implement other packet types
          default:
            log.error("Unsupported command type: %s", commandType);
            break;
        }
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

  private void handleConnect(OutputStream output)
      throws IOException {
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

  private void handlePingreq(PingReqPacket packet, OutputStream output)
      throws IOException {
    // PINGREQ is a fixed two-byte packet
    if (packet.getFixedHeader().remainingLength() != 0) {
      log.error("%s:%s : Invalid PINGREQ packet received", inetAddress.toString(), port);
      return;
    }

    // Construct the PINGRESP packet
    byte[] pingRespPacket = {
        (byte) 0xD0, // PINGRESP packet type
        (byte) 0x00  // Remaining length
    };

    // Send the PINGRESP packet back to the client
    output.write(pingRespPacket);
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
