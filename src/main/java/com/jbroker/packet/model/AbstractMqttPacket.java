package com.jbroker.packet.model;

import com.jbroker.command.CommandType;
import com.jbroker.packet.model.header.FixedHeader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractMqttPacket implements MqttPacket {

  private final FixedHeader fixedHeader;

  @Override
  public CommandType getCommandType() {
    return fixedHeader.getCommandType();
  }
}
