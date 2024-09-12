package com.jbroker.packet;

public record FixedHeader(
    int controlPacketType,
    int remainingLength) {

  public static final int CONTROL_PACKET_TYPE_POSITION = 1;
  public static final int REMAINING_LENGTH_START_POSITION = 2;

}
