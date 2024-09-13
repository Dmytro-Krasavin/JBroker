package com.jbroker.packet;

public class PingReqPacket extends MqttPacket {

  // PINGREQ is a fixed two-byte packet
  public static final byte PINGREQ_REMAINING_LENGTH = 0;

  // PINGREQ packet has neither a Variable Header nor a Payload

  public PingReqPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
