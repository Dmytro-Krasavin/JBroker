package com.jbroker.packet.model.outbound.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.AbstractMqttPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;

public class PingRespPacket extends AbstractMqttPacket implements ServerToClientPacket {

  // PINGRESP is a fixed two-byte packet
  public static final int PINGRESP_REMAINING_LENGTH = 0;

  // PINGRESP packet has neither a Variable Header nor a Payload

  public PingRespPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
