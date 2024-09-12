package com.jbroker.packet;

public class DisconnectPacket extends MqttPacket {

  // The DISCONNECT packet has neither a variable header nor a payload

  public DisconnectPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
