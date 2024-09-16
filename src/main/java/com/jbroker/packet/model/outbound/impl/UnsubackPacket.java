package com.jbroker.packet.model.outbound.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.AbstractMqttPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;
import lombok.Getter;

@Getter
public class UnsubackPacket extends AbstractMqttPacket implements ServerToClientPacket {

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
