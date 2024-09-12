package com.jbroker.packet;

import com.jbroker.command.CommandType;
import lombok.Getter;

@Getter
public class ConnackPacket extends MqttPacket {

  // CONNACK is a fixed four-byte packet (2 for fixed header and 2 for variable header)
  public static final byte CONNACK_REMAINING_LENGTH = 2;

  // Byte positions in the CONNECT packet (counting after Fixed Header bytes)
  public static final int CONNECT_ACKNOWLEDGE_FLAGS_POSITION = 1;
  public static final int RETURN_CODE_POSITION = 2;

  // Connect Acknowledge Flags bit positions
  public static final int SESSION_PRESENT_BIT = 0;

  // Variable
  //    Connect Acknowledge Flags         // byte 1
  //    Reserved bits                     // byte 1 (bits 7-1)
  private final boolean sessionPresent;   // byte 1 (bit 0)
  private final byte returnCode;          // byte 2

  public ConnackPacket(
      FixedHeader fixedHeader,
      boolean sessionPresent,
      ConnectReturnCode returnCode) {
    super(CommandType.CONNACK, fixedHeader);
    this.sessionPresent = sessionPresent;
    this.returnCode = returnCode.getValue();
  }

  @Getter
  public enum ConnectReturnCode {
    // Connection accepted
    UNACCEPTABLE_PROTOCOL_VERSION((byte) 1),

    // The Server does not support the level of the MQTT protocol requested by the Client
    IDENTIFIER_REJECTED((byte) 2),

    // The Client identifier is correct UTF-8 but not allowed by the Server
    SERVER_UNAVAILABLE((byte) 3),

    // The Network Connection has been made but the MQTT service is unavailable
    BAD_USER_NAME_OR_PASSWORD((byte) 4),

    // The data in the username or password is malformed
    NOT_AUTHORIZED((byte) 5),

    // The Client is not authorized to connect
    CONNECTION_ACCEPTED((byte) 0),
    ;

    private final byte value;

    ConnectReturnCode(byte value) {
      this.value = value;
    }
  }
}
