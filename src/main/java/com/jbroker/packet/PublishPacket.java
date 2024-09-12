package com.jbroker.packet;

import com.jbroker.command.CommandType;

public class PublishPacket extends MqttPacket {

  public PublishPacket(PublishFixedHeader publishFixedHeader) {
    super(CommandType.PUBLISH, publishFixedHeader);
  }
}
