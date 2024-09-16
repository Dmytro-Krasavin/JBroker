package com.jbroker.command;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommandDispatcher {

  private final CommandHandlerFactory commandHandlerFactory;

  public Optional<ServerToClientPacket> dispatchCommand(
      ClientToServerPacket inboundPacket,
      String clientId) {
    CommandHandler<ClientToServerPacket, ServerToClientPacket> commandHandler = commandHandlerFactory.getCommandHandler(
        inboundPacket.getCommandType()
    );
    log.info("{}: executing {} command", clientId, inboundPacket.getCommandType());
    return commandHandler.handleCommand(inboundPacket, clientId);
  }
}
