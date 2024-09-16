package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.CONNACK;
import static com.jbroker.packet.model.outbound.impl.ConnackPacket.CONNACK_REMAINING_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.model.outbound.impl.ConnackPacket;
import com.jbroker.packet.model.outbound.impl.ConnackPacket.ConnectReturnCode;
import com.jbroker.packet.model.inbound.impl.ConnectPacket;
import com.jbroker.packet.model.header.FixedHeader;
import java.util.Optional;

public class ConnectHandler implements CommandHandler<ConnectPacket, ConnackPacket> {

  @Override
  public Optional<ConnackPacket> handleCommand(ConnectPacket inboundPacket, String clientId) {
    FixedHeader fixedHeader = new FixedHeader(CONNACK.getValue(), CONNACK_REMAINING_LENGTH);
    // TODO: Implement 3.2.2.2 Session Present: https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718035
    boolean sessionPresent = !inboundPacket.isCleanSession();
    return Optional.of(
        new ConnackPacket(fixedHeader, sessionPresent, ConnectReturnCode.CONNECTION_ACCEPTED)
    );
  }
}
