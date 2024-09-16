package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.PINGRESP;
import static com.jbroker.packet.model.inbound.impl.PingReqPacket.PINGREQ_REMAINING_LENGTH;
import static com.jbroker.packet.model.outbound.impl.PingRespPacket.PINGRESP_REMAINING_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.impl.PingReqPacket;
import com.jbroker.packet.model.outbound.impl.PingRespPacket;
import java.util.Optional;

public class PingReqHandler implements CommandHandler<PingReqPacket, PingRespPacket> {

  @Override
  public Optional<PingRespPacket> handleCommand(PingReqPacket packet, String clientId) {
    if (packet.getFixedHeader().getRemainingLength() != PINGREQ_REMAINING_LENGTH) {
      throw new IllegalArgumentException("Invalid PINGREQ packet received");
    }
    return Optional.of(new PingRespPacket(
        new FixedHeader(PINGRESP.getValue(), PINGRESP_REMAINING_LENGTH)
    ));
  }
}
