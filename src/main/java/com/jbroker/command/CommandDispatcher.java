package com.jbroker.command;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.packet.MqttPacket;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandDispatcher {

  private final CommandHandlerFactory commandHandlerFactory;

  public <InboundPacket extends MqttPacket, OutboundPacket extends MqttPacket> Optional<OutboundPacket> dispatchCommand(
      InboundPacket inboundPacket) {
    // TODO: fix this generic stuff
    CommandHandler<InboundPacket, OutboundPacket> commandHandler =
        (CommandHandler<InboundPacket, OutboundPacket>) commandHandlerFactory.getCommandHandler(
            inboundPacket.getCommandType());
    return commandHandler.handleCommand(inboundPacket);
  }
}
