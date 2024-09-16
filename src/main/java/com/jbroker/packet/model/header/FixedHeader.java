package com.jbroker.packet.model.header;

import com.jbroker.command.CommandType;
import lombok.Getter;

@Getter
public class FixedHeader {

  // Byte positions in the Fixed Header
  public static final int CONTROL_PACKET_TYPE_POSITION = 1;
  public static final int REMAINING_LENGTH_START_POSITION = 2;

  private final int controlPacketType;
  private final int remainingLength;

  /**
   * Represents the command type associated with this MQTT packet. This field is not present in the
   * actual MQTT packet but is used internally to help identify and handle different types of MQTT
   * packets.
   */
  private final CommandType commandType;

  public FixedHeader(int controlPacketType, int remainingLength) {
    this.controlPacketType = controlPacketType;
    this.remainingLength = remainingLength;
    this.commandType = CommandType.resolveType(controlPacketType);
  }
}
