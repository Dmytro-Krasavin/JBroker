package com.jbroker.client;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientConnectionManager {

  private final PacketReader packetReader;
  private final PacketWriter packetWriter;
  private final CommandDispatcher commandDispatcher;
  private final ClientConnectionRegistry clientConnectionRegistry;

  public ClientConnection createClientConnection(Socket clientSocket) throws IOException {
    SocketAddress clientSocketAddress = clientSocket.getRemoteSocketAddress();
    ClientConnection clientConnection = new SocketConnection(
        clientSocket,
        packetReader,
        packetWriter,
        commandDispatcher,
        onCloseConnectionCallback(clientSocketAddress)
    );
    clientConnectionRegistry.addClientConnection(clientSocketAddress, clientConnection);
    return clientConnection;
  }

  private Runnable onCloseConnectionCallback(SocketAddress clientSocketAddress) {
    return () -> clientConnectionRegistry.removeClientConnection(clientSocketAddress);
  }
}
