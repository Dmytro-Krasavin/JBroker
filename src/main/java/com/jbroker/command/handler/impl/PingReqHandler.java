package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.PINGRESP;
import static com.jbroker.packet.model.inbound.impl.PingReqPacket.PINGREQ_REMAINING_LENGTH;
import static com.jbroker.packet.model.outbound.impl.PingRespPacket.PINGRESP_REMAINING_LENGTH;

import com.jbroker.command.handler.AbstractCommandHandler;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.impl.PingReqPacket;
import com.jbroker.packet.model.outbound.impl.PingRespPacket;

public class PingReqHandler extends AbstractCommandHandler<PingReqPacket, PingRespPacket> {

  @Override
  protected PingRespPacket getOutboundPacket(PingReqPacket pingReqPacket) {
    if (pingReqPacket.getFixedHeader().getRemainingLength() != PINGREQ_REMAINING_LENGTH) {
      throw new IllegalArgumentException("Invalid PINGREQ packet received");
    }
    return new PingRespPacket(
        new FixedHeader(PINGRESP.getValue(), PINGRESP_REMAINING_LENGTH)
    );
  }
}
