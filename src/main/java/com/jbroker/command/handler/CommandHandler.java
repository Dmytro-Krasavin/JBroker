package com.jbroker.command.handler;

import com.jbroker.packet.MqttPacket;
import java.util.Optional;

public interface CommandHandler<InboundPacket extends MqttPacket, OutboundPacket extends MqttPacket> {

  // TODO: pass client info, like clientId
  Optional<OutboundPacket> handleCommand(InboundPacket inboundPacket);
}
