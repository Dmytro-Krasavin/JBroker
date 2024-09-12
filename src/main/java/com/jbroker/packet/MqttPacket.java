package com.jbroker.packet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class MqttPacket {

  public static final Charset TEXT_FIELD_ENCODING = StandardCharsets.UTF_8;

  private final FixedHeader fixedHeader;
}
