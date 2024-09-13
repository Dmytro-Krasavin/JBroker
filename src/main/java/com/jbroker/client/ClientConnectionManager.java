package com.jbroker.client;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.net.Socket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientConnectionManager {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public ClientConnection createClientConnection(Socket clientSocket) throws IOException {
    ClientConnection clientConnection = new ClientConnection(
        clientSocket,
        packetReader,
        packetWriter,
        commandDispatcher
    );
    clientConnectionRegistry.addClientConnection(
        clientSocket.getRemoteSocketAddress(),
        clientConnection
    );
    return clientConnection;
  }
}
