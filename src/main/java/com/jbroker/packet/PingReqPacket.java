package com.jbroker.packet;

import com.jbroker.command.CommandType;

public class PingReqPacket extends MqttPacket {

  // PINGREQ is a fixed two-byte packet
  public static final byte PINGREQ_REMAINING_LENGTH = 0;

  // PINGREQ packet has neither a variable header nor a payload

  public PingReqPacket(FixedHeader fixedHeader) {
    super(CommandType.PINGREQ, fixedHeader);
  }
}
