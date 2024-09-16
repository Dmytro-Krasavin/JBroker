package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownCommandHandler implements
    CommandHandler<ClientToServerPacket, ServerToClientPacket> {

  @Override
  public Optional<ServerToClientPacket> handleCommand(
      ClientToServerPacket inboundPacket,
      String clientId) {
    log.error("Unknown command received! Command type: {}", inboundPacket.getCommandType());
    return Optional.empty();
  }
}
