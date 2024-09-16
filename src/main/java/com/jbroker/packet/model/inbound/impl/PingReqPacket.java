package com.jbroker.packet.model.inbound.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.AbstractMqttPacket;
import com.jbroker.packet.model.inbound.ClientToServerPacket;

public class PingReqPacket extends AbstractMqttPacket implements ClientToServerPacket {

  // PINGREQ is a fixed two-byte packet
  public static final byte PINGREQ_REMAINING_LENGTH = 0;

  // PINGREQ packet has neither a Variable Header nor a Payload

  public PingReqPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
