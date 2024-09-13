package com.jbroker.client;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.net.Socket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientConnectionFactory {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;

  public ClientConnection createClientConnection(Socket clientSocket) throws IOException {
    return new ClientConnection(clientSocket, packetReader, packetWriter, commandDispatcher);
  }
}
