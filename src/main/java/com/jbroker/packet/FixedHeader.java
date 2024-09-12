package com.jbroker.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FixedHeader {

  // Byte positions in the Fixed Header
  public static final int CONTROL_PACKET_TYPE_POSITION = 1;
  public static final int REMAINING_LENGTH_START_POSITION = 2;

  private final int controlPacketType;
  private final int remainingLength;
}
