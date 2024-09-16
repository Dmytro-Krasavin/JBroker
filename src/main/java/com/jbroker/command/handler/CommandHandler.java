package com.jbroker.command.handler;

import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;

public interface CommandHandler<In extends ClientToServerPacket, Out extends ServerToClientPacket> {

  Optional<Out> handleCommand(In inboundPacket, String clientId);
}
