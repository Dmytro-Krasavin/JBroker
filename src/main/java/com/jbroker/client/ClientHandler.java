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
import java.net.Socket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends Thread {

  private static final int CLIENT_CLOSED_SOCKET_CONNECTION = -1;

  private final Socket socket;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;

  public ClientHandler(
      Socket socket,
      PacketReader packetReader,
      PacketWriter packetWriter,
      CommandDispatcher commandDispatcher) {
    this.socket = socket;
    this.packetReader = packetReader;
    this.packetWriter = packetWriter;
    this.commandDispatcher = commandDispatcher;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public void run() {
    log.info("{}:{}: New socket connection", socket.getInetAddress().toString(), socket.getPort());
    try (InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream()) {
      while (listenForIncomingPackets(input, output))
        ;
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    } catch (PacketSendFailedException e) {
      log.error("Failed to send {} packet to client {}:{}. Reason: {}",
          e.getMessage(), socket.getInetAddress(), socket.getPort(), e.getCause().toString()
      );
    } finally {
      closeSocket();
    }
  }

  private boolean listenForIncomingPackets(InputStream input, OutputStream output)
      throws IOException {
    //  java.io.InputStream.read() blocks until input data is available,
    //  the end of the stream is detected, or an exception is thrown.
    int firstByte = input.read();
    boolean clientClosedConnection = didClientCloseSocketConnection(firstByte);
    if (clientClosedConnection) {
      log.info("{}:{}: Client closed socket connection", socket.getInetAddress(), socket.getPort());
      return false;
    }

    MqttPacket inboundPacket = packetReader.read(firstByte, input);
    Optional<MqttPacket> outboundPacket = commandDispatcher.dispatchCommand(inboundPacket);
    outboundPacket.ifPresent(packet -> packetWriter.write(output, packet));

    return didClientCloseMqttConnection(inboundPacket);
  }

  private void closeSocket() {
    try {
      socket.close();
      log.info("{}:{}: Socket connection closed", socket.getInetAddress(), socket.getPort());
    } catch (IOException e) {
      log.error("IOException occurred while closing socket: {}", e.getMessage());
    }
  }

  private boolean didClientCloseSocketConnection(int firstByte) {
    return firstByte == CLIENT_CLOSED_SOCKET_CONNECTION;
  }

  private static boolean didClientCloseMqttConnection(MqttPacket inboundPacket) {
    return inboundPacket.getFixedHeader().getCommandType() != CommandType.DISCONNECT;
  }
}
