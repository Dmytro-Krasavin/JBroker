package com.jbroker.client;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.CommandType;
import com.jbroker.exception.PacketSendFailedException;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends Thread {

  private final Socket socket;
  private final InetAddress inetAddress;
  private final int port;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;

  public ClientHandler(
      Socket socket,
      PacketReader packetReader,
      PacketWriter packetWriter,
      CommandDispatcher commandDispatcher) {
    this.socket = socket;
    this.inetAddress = socket.getInetAddress();
    this.port = socket.getPort();
    this.packetReader = packetReader;
    this.packetWriter = packetWriter;
    this.commandDispatcher = commandDispatcher;
  }

  @Override
  public void run() {
    log.info("{}:{}: New socket connection", inetAddress.toString(), port);
    try (InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream()) {

      // Main loop to handle incoming packets
      while (true) {
        if (input.available() == 0) {
          // End of stream reached
          continue;
        }

        MqttPacket inboundPacket = packetReader.read(input);
        Optional<MqttPacket> outboundPacket = commandDispatcher.dispatchCommand(inboundPacket);
        outboundPacket.ifPresent(packet -> packetWriter.write(output, packet));

        if (inboundPacket.getCommandType() == CommandType.DISCONNECT) {
          return; // Exit the loop and close the socket
        }
      }
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    } catch (PacketSendFailedException e) {
      log.error("Failed to send {} packet to client {}:{}. Reason: {}",
          e.getMessage(), inetAddress.toString(), port, e.getCause().toString()
      );
    } finally {
      try {
        socket.close();
        log.info("{}:{}: Socket connection closed", inetAddress, port);
      } catch (IOException e) {
        log.error("IOException occurred while closing socket: {}", e.getMessage());
      }
    }
  }
}
