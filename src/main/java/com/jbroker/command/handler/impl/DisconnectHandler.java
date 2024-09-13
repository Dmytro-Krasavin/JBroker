package com.jbroker.command.handler.impl;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.DisconnectPacket;
import com.jbroker.packet.MqttPacket;
import java.util.Optional;

public class DisconnectHandler implements CommandHandler<DisconnectPacket, MqttPacket> {

  @Override
  public Optional<MqttPacket> handleCommand(DisconnectPacket disconnectPacket, String clientId) {
    // do nothing for now
    return Optional.empty();
  }
}
