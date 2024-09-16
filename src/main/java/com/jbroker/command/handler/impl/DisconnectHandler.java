package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.model.inbound.impl.DisconnectPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;

public class DisconnectHandler implements CommandHandler<DisconnectPacket, ServerToClientPacket> {

  @Override
  public Optional<ServerToClientPacket> handleCommand(
      DisconnectPacket disconnectPacket,
      String clientId) {
    // do nothing for now
    return Optional.empty();
  }
}
