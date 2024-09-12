package com.jbroker;

import com.jbroker.packet.reader.PacketReader;
import java.net.Socket;

public class ClientHandlerFactory {

  private final PacketReader packetReader;

  public ClientHandlerFactory(PacketReader packetReader) {
    this.packetReader = packetReader;
  }

  public ClientHandler createClientHandler(Socket clientSocket) {
    return new ClientHandler(clientSocket, packetReader);
  }
}
