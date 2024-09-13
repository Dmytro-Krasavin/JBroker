package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.MqttPacket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownCommandHandler implements CommandHandler<MqttPacket, MqttPacket> {

  @Override
  public Optional<MqttPacket> handleCommand(MqttPacket inboundPacket, String clientId) {
    log.error("Unknown command received! Command type: {}", inboundPacket.getCommandType());
    return Optional.empty();
  }
}
