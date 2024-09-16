package com.jbroker.client;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.CommandType;
import com.jbroker.exception.PacketSendFailedException;
import com.jbroker.packet.ConnectPacket;
import com.jbroker.packet.MqttPacket;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
  private final Runnable onCloseConnectionCallback;
  private final Lock lock = new ReentrantLock();

  @Getter
  private String clientId;

  public SocketConnection(
      Socket socket,
      PacketReader packetReader,
      PacketWriter packetWriter,
      CommandDispatcher commandDispatcher,
      Runnable onCloseConnectionCallback) throws IOException {
    this.socket = socket;
    this.outputStream = socket.getOutputStream();
    this.packetReader = packetReader;
    this.packetWriter = packetWriter;
    this.commandDispatcher = commandDispatcher;
    this.onCloseConnectionCallback = onCloseConnectionCallback;
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
  public void sentPacket(MqttPacket outboundPacket) {
    lock.lock();
    try {
      log.info("Sending {} packet to client '{}'", outboundPacket.getCommandType(), clientId);
      packetWriter.write(outputStream, outboundPacket);
    } finally {
      lock.unlock();
    }
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

    MqttPacket inboundPacket = packetReader.read(firstByte, input);
    Optional<MqttPacket> outboundPacket = commandDispatcher.dispatchCommand(
        inboundPacket,
        clientId != null ? clientId : socket.getRemoteSocketAddress().toString()
    );
    outboundPacket.ifPresent(this::sentPacket);

    if (clientId == null && inboundPacket instanceof ConnectPacket connectPacket) {
      clientId = connectPacket.getClientId();
      log.info("New MQTT connection! Client id: '{}'", clientId);
    }

    boolean clientClosedMqttConnection = didClientCloseMqttConnection(inboundPacket);
    if (clientClosedMqttConnection) {
      log.info("{}: Client closed MQTT connection!", clientId);
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
    onCloseConnectionCallback.run();
  }

  private boolean didClientCloseSocketConnection(int firstByte) {
    return firstByte == CLIENT_CLOSED_SOCKET_CONNECTION;
  }

  private static boolean didClientCloseMqttConnection(MqttPacket inboundPacket) {
    return inboundPacket.getCommandType() == CommandType.DISCONNECT;
  }
}
