package com.jbroker.command;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandType {

  CONNECT(1),        // Client request to connect to Server
  CONNACK(2),        // Connect acknowledgment
  PUBLISH(3),        // Publish message
  PUBACK(4),         // Publish acknowledgment
  PUBREC(5),         // Publish received (assured delivery part 1)
  PUBREL(6),         // Publish release (assured delivery part 2)
  PUBCOMP(7),        // Publish complete (assured delivery part 3)
  SUBSCRIBE(8),      // Client subscribe request
  SUBACK(9),         // Subscribe acknowledgment
  UNSUBSCRIBE(10),   // Unsubscribe request
  UNSUBACK(11),      // Unsubscribe acknowledgment
  PINGREQ(12),       // PING request
  PINGRESP(13),      // Unsubscribe response
  DISCONNECT(14);    // Client is disconnecting

  private final int value;

  public static CommandType resolveType(int controlPacketType) {
    return Arrays.stream(values()).filter(type -> type.getValue() == controlPacketType)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
