package com.jbroker;

import com.jbroker.packet.parser.PacketParserDispatcher;
import java.net.Socket;

public class ClientHandlerFactory {

  private final PacketParserDispatcher packetParser;

  public ClientHandlerFactory(PacketParserDispatcher packetParser) {
    this.packetParser = packetParser;
  }

  public ClientHandler createClientHandler(Socket clientSocket) {
    return new ClientHandler(clientSocket, packetParser);
  }
}
