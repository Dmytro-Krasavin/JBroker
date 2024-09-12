package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.logger.Logger;
import com.jbroker.packet.MqttPacket;
import java.util.Optional;

public class UnknownCommandHandler implements CommandHandler<MqttPacket, MqttPacket> {

  private final Logger log = Logger.getInstance();

  @Override
  public Optional<MqttPacket> handleCommand(MqttPacket inboundPacket) {
    log.error("Unknown command received! Command type: %s", inboundPacket.getCommandType());
    return Optional.empty();
  }
}
