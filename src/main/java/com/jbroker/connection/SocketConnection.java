package com.jbroker.connection;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.CommandType;
import com.jbroker.exception.PacketSendFailedException;
import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.inbound.impl.ConnectPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketConnection implements ClientConnection {

  private static final int CLIENT_CLOSED_SOCKET_CONNECTION = -1;

  private final Socket socket;
  private final OutputStream outputStream;
  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;
  private final Lock lock = new ReentrantLock();

  @Getter
  private String mqttClientId;

  public SocketConnection(
      Socket socket,
      PacketReader packetReader,
      PacketWriter packetWriter,
      CommandDispatcher commandDispatcher) throws IOException {
    this.socket = socket;
    this.outputStream = socket.getOutputStream();
    this.packetReader = packetReader;
    this.packetWriter = packetWriter;
    this.commandDispatcher = commandDispatcher;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public void run() {
    log.info("New socket connection! Client socket address: {}", socket.getRemoteSocketAddress());
    try (InputStream inputStream = socket.getInputStream()) {
      while (listenForIncomingPackets(inputStream))
        ;
    } catch (IOException e) {
      log.error("IOException occurred: {}", e.getMessage());
    } catch (PacketSendFailedException e) {
      log.error("Failed to send {} packet to client {}. Reason: {}",
          e.getCommandType().name(), socket.getRemoteSocketAddress(), e.getCause().toString()
      );
    } finally {
      closeSocket();
    }
  }

  @Override
  public void sentPacket(ServerToClientPacket outboundPacket) {
    lock.lock();
    try {
      log.info("Sending {} packet to client '{}'", outboundPacket.getCommandType(), mqttClientId);
      packetWriter.write(outputStream, outboundPacket);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public SocketAddress getSocketAddress() {
    return socket.getRemoteSocketAddress();
  }

  private boolean listenForIncomingPackets(InputStream input)
      throws IOException {
    //  java.io.InputStream.read() blocks until input data is available,
    //  the end of the stream is detected, or an exception is thrown.
    int firstByte = input.read();
    if (didClientCloseSocketConnection(firstByte)) {
      log.info("{}: Client closed socket connection!", socket.getRemoteSocketAddress());
      return false;
    }

    ClientToServerPacket inboundPacket = packetReader.read(firstByte, input);
    Optional<ServerToClientPacket> outboundPacket = commandDispatcher.dispatchCommand(
        inboundPacket,
        mqttClientId != null ? mqttClientId : socket.getRemoteSocketAddress().toString()
    );
    outboundPacket.ifPresent(this::sentPacket);

    if (mqttClientId == null && inboundPacket instanceof ConnectPacket connectPacket) {
      mqttClientId = connectPacket.getClientId();
      log.info("New MQTT connection! Client id: '{}'", mqttClientId);
    }

    boolean clientClosedMqttConnection = didClientCloseMqttConnection(inboundPacket);
    if (clientClosedMqttConnection) {
      log.info("{}: Client closed MQTT connection!", mqttClientId);
      return false;
    }
    return true;
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

  private static boolean didClientCloseMqttConnection(ClientToServerPacket inboundPacket) {
    return inboundPacket.getCommandType() == CommandType.DISCONNECT;
  }
}
