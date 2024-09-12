package com.jbroker.packet;

import com.jbroker.command.CommandType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class MqttPacket {

  public static final Charset TEXT_FIELD_ENCODING = StandardCharsets.UTF_8;

  /**
   * Represents the command type associated with this MQTT packet. This field is not present in the
   * actual MQTT packet but is used internally to help identify and handle different types of MQTT
   * packets.
   */
  private final CommandType commandType;

  private final FixedHeader fixedHeader;
}
