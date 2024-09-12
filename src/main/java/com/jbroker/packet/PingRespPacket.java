package com.jbroker.packet;

import com.jbroker.command.CommandType;

public class PingRespPacket extends MqttPacket {

  // PINGRESP is a fixed two-byte packet
  public static final int PINGRESP_REMAINING_LENGTH = 0;

  // PINGRESP packet has neither a variable header nor a payload

  public PingRespPacket(FixedHeader fixedHeader) {
    super(CommandType.PINGRESP, fixedHeader);
  }
}
