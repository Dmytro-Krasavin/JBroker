package com.jbroker.packet;

public class PingReqPacket extends MqttPacket {

  // The PINGREQ packet has neither a variable header nor a payload

  public PingReqPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
