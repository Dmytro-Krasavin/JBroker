package com.jbroker.command;

import static com.jbroker.command.CommandDirection.CLIENT_TO_SERVER;
import static com.jbroker.command.CommandDirection.SERVER_TO_CLIENT;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum CommandType {

  CONNECT((byte) 1,
      List.of(CLIENT_TO_SERVER)),                     // Client request to connect to Server
  CONNACK((byte) 2,
      List.of(SERVER_TO_CLIENT)),                     // Connect acknowledgment
  PUBLISH((byte) 3,
      List.of(CLIENT_TO_SERVER, SERVER_TO_CLIENT)),   // Publish message
  PUBACK((byte) 4,
      List.of(CLIENT_TO_SERVER, SERVER_TO_CLIENT)),   // Publish acknowledgment
  PUBREC((byte) 5,
      List.of(CLIENT_TO_SERVER, SERVER_TO_CLIENT)),   // Publish received (assured delivery part 1)
  PUBREL((byte) 6,
      List.of(CLIENT_TO_SERVER, SERVER_TO_CLIENT)),   // Publish release (assured delivery part 2)
  PUBCOMP((byte) 7,
      List.of(CLIENT_TO_SERVER, SERVER_TO_CLIENT)),   // Publish complete (assured delivery part 3)
  SUBSCRIBE((byte) 8,
      List.of(CLIENT_TO_SERVER)),                     // Client subscribe request
  SUBACK((byte) 9,
      List.of(SERVER_TO_CLIENT)),                     // Subscribe acknowledgment
  UNSUBSCRIBE((byte) 10,
      List.of(CLIENT_TO_SERVER)),                     // Unsubscribe request
  UNSUBACK((byte) 11,
      List.of(SERVER_TO_CLIENT)),                     // Unsubscribe acknowledgment
  PINGREQ((byte) 12,
      List.of(CLIENT_TO_SERVER)),                     // PING request
  PINGRESP((byte) 13,
      List.of(SERVER_TO_CLIENT)),                     // Unsubscribe response
  DISCONNECT((byte) 14,
      List.of(CLIENT_TO_SERVER));                     // Client is disconnecting

  private final byte value;
  private final List<CommandDirection> directions;

  CommandType(byte value, List<CommandDirection> directions) {
    this.value = value;
    this.directions = directions;
  }

  public static CommandType resolveType(int controlPacketType) {
    return Arrays.stream(values()).filter(type -> type.getValue() == controlPacketType)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
