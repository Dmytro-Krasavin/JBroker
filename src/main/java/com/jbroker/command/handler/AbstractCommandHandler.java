package com.jbroker.command.handler;

import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import java.util.Optional;

public abstract class AbstractCommandHandler<In extends ClientToServerPacket, Out extends ServerToClientPacket>
    implements CommandHandler<In, Out> {

  @Override
  public final Optional<Out> handleCommand(In inboundPacket, String clientId) {
    Out outboundPacket = getOutboundPacket(inboundPacket);
    doSideEffects(inboundPacket, clientId);
    return Optional.ofNullable(outboundPacket);
  }

  protected Out getOutboundPacket(In inboundPacket) {
    return null;
  }

  protected void doSideEffects(In inboundPacket, String clientId) {
  }
}
