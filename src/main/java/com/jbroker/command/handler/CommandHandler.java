package com.jbroker.command.handler;

import com.jbroker.packet.MqttPacket;
import java.util.Optional;

public interface CommandHandler<InboundPacket extends MqttPacket, OutboundPacket extends MqttPacket> {

  Optional<OutboundPacket> handleCommand(InboundPacket inboundPacket, String clientId);
}
