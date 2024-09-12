package com.jbroker.packet;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConnectPacket extends MqttPacket {

  // Constants for byte positions in the CONNECT packet
  public static final int PROTOCOL_NAME_START_POSITION = 1;
  public static final int PROTOCOL_NAME_LENGTH = 4; // "MQTT" = 4 bytes
  public static final int PROTOCOL_LEVEL_POSITION = 7;
  public static final int CONNECT_FLAGS_POSITION = 8;
  public static final int KEEP_ALIVE_START_POSITION = 9;
  public static final int KEEP_ALIVE_END_POSITION = 10;
  public static final int CLIENT_ID_START_POSITION = 11;

  // Constants for flag bit positions
  public static final int USERNAME_FLAG_BIT = 7;
  public static final int PASSWORD_FLAG_BIT = 6;
  public static final int WILL_RETAIN_FLAG_BIT = 5;
  public static final int WILL_QOS_BITS = 0b00011000; // Bits 3-4 for QoS
  public static final int WILL_FLAG_BIT = 2;
  public static final int CLEAN_SESSION_FLAG_BIT = 1;

  // Variable header:
  //    Packet identifier MSB (Most Significant Byte)    // byte 1
  //    Packet identifier LSB (Least Significant Byte)   // byte 2
  private final String protocolName;                     // bytes 3-6
  private final byte protocolLevel;                      // byte 7
  private final boolean userNameFlag;                    // byte 8 (bit 7)
  private final boolean passwordFlag;                    // byte 8 (bit 6)
  private final boolean willRetain;                      // byte 8 (bit 5)
  private final int willQoS;                             // byte 8 (bit 3-4)
  private final boolean willFlag;                        // byte 8 (bit 2)
  private final boolean cleanSession;                    // byte 8 (bit 1)
  //    Reserved bit                                     // byte 8 (bit 0)
  //    Keep Alive MSB                                   // byte 9
  //    Keep Alive LSB                                   // byte 10
  private final int keepAlive;                           // bytes 9-10
  // TODO: If the Keep Alive value is non-zero and the Server does not receive a Control Packet
  //  from the Client within one and a half times the Keep Alive time period,
  //  it MUST disconnect the Network Connection to the Client as if the network had failed [MQTT-3.1.2-24].
  // TODO: A Keep Alive value of zero (0) has the effect of turning off the keep alive mechanism.
  //  This means that, in this case, the Server is not required to disconnect the Client on the grounds of inactivity.
  //  Note that a Server is permitted to disconnect a Client that it determines to be inactive or non-responsive at any time,
  //  regardless of the Keep Alive value provided by that Client.

  // Payload:
  private final String clientId;
  private final String willTopic;
  private final String willMessage;
  private final String userName;
  private final String password;

  @Builder
  private ConnectPacket(
      FixedHeader fixedHeader,
      String protocolName,
      byte protocolLevel,
      boolean userNameFlag,
      boolean passwordFlag,
      boolean willRetain,
      int willQoS,
      boolean willFlag,
      boolean cleanSession,
      int keepAlive,
      String clientId,
      String willTopic,
      String willMessage,
      String userName,
      String password) {
    super(fixedHeader);
    this.protocolName = protocolName;
    this.protocolLevel = protocolLevel;
    this.userNameFlag = userNameFlag;
    this.passwordFlag = passwordFlag;
    this.willRetain = willRetain;
    this.willQoS = willQoS;
    this.willFlag = willFlag;
    this.cleanSession = cleanSession;
    this.keepAlive = keepAlive;
    this.clientId = clientId;
    this.willTopic = willTopic;
    this.willMessage = willMessage;
    this.userName = userName;
    this.password = password;
  }
}
