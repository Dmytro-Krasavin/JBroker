package com.jbroker.packet;

public class PingRespPacket extends MqttPacket {

  // PINGRESP is a fixed two-byte packet
  public static final int PINGRESP_REMAINING_LENGTH = 0;

  // PINGRESP packet has neither a Variable Header nor a Payload

  public PingRespPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
