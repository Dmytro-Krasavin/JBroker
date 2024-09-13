package com.jbroker.packet;

import lombok.Getter;

@Getter
public class UnsubackPacket extends MqttPacket {

  // UNSUBACK is a fixed four-byte packet (2 for fixed header and 2 for variable header)
  public static final byte UNSUBACK_REMAINING_LENGTH = 2;

  // Variable Header
  private final int packetIdentifier;

  // UNSUBACK packet has no Payload

  public UnsubackPacket(FixedHeader fixedHeader, int packetIdentifier) {
    super(fixedHeader);
    this.packetIdentifier = packetIdentifier;
  }
}
