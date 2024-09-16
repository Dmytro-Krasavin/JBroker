package com.jbroker.packet.model;

import com.jbroker.command.CommandType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface MqttPacket {

  Charset TEXT_FIELD_ENCODING = StandardCharsets.UTF_8;

  CommandType getCommandType();
}
