package com.jbroker.packet.model.inbound.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.AbstractMqttPacket;
import com.jbroker.packet.model.inbound.ClientToServerPacket;

public class DisconnectPacket extends AbstractMqttPacket implements ClientToServerPacket {

  // DISCONNECT packet has neither a Variable Header nor a Payload

  public DisconnectPacket(FixedHeader fixedHeader) {
    super(fixedHeader);
  }
}
