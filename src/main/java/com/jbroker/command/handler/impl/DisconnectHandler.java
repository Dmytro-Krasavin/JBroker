package com.jbroker.command.handler.impl;

import com.jbroker.connection.registry.ClientConnectionRegistry;
import com.jbroker.command.handler.AbstractCommandHandler;
import com.jbroker.packet.model.inbound.impl.DisconnectPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DisconnectHandler extends
    AbstractCommandHandler<DisconnectPacket, ServerToClientPacket> {

  private final ClientConnectionRegistry clientConnectionRegistry;

  @Override
  protected void doSideEffects(DisconnectPacket inboundPacket, String clientId) {
    clientConnectionRegistry.removeClientConnection(clientId);
  }
}
