package com.jbroker.command;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.packet.MqttPacket;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommandDispatcher {

  private final CommandHandlerFactory commandHandlerFactory;

  public <InboundPacket extends MqttPacket, OutboundPacket extends MqttPacket> Optional<OutboundPacket> dispatchCommand(
      InboundPacket inboundPacket, String clientId) {
    // TODO: fix this generic stuff
    CommandHandler<InboundPacket, OutboundPacket> commandHandler =
        (CommandHandler<InboundPacket, OutboundPacket>) commandHandlerFactory.getCommandHandler(
            inboundPacket.getFixedHeader().getCommandType());
    log.info("{}: executing {} command", clientId, inboundPacket.getFixedHeader().getCommandType());
    return commandHandler.handleCommand(inboundPacket, clientId);
  }
}
