package com.jbroker.packet;

public class DisconnectPacket extends MqttPacket {

  // DISCONNECT packet has neither a Variable Header nor a Payload

  public DisconnectPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
