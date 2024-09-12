package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.PINGRESP;
import static com.jbroker.packet.PingReqPacket.PINGREQ_REMAINING_LENGTH;
import static com.jbroker.packet.PingRespPacket.PINGRESP_REMAINING_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PingReqPacket;
import com.jbroker.packet.PingRespPacket;
import java.util.Optional;

public class PingReqHandler implements CommandHandler<PingReqPacket, PingRespPacket> {

  @Override
  public Optional<PingRespPacket> handleCommand(PingReqPacket packet) {
    if (packet.getFixedHeader().getRemainingLength() != PINGREQ_REMAINING_LENGTH) {
      throw new IllegalArgumentException("Invalid PINGREQ packet received");
    }
    return Optional.of(new PingRespPacket(
        new FixedHeader(PINGRESP.getValue(), PINGRESP_REMAINING_LENGTH)
    ));
  }
}
