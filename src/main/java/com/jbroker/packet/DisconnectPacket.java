package com.jbroker.packet;

import com.jbroker.command.CommandType;

public class DisconnectPacket extends MqttPacket {

  // DISCONNECT packet has neither a variable header nor a payload

  public DisconnectPacket(FixedHeader fixedHeader) {
    super(CommandType.DISCONNECT, fixedHeader);
  }
}
