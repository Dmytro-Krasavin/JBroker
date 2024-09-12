package com.jbroker;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.net.Socket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientHandlerFactory {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;

  public ClientHandler createClientHandler(Socket clientSocket) {
    return new ClientHandler(clientSocket, packetReader, packetWriter, commandDispatcher);
  }
}
