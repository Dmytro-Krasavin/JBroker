package com.jbroker.packet;

import java.util.Map;
import lombok.Getter;

@Getter
public class SubscribePacket extends MqttPacket {

  /**
   * From MQTT specification: <i>Bits 3,2,1 and 0 of the fixed header of the SUBSCRIBE Control
   * Packet are reserved and MUST be set to 0,0,1 and 0 respectively. The Server MUST treat any
   * other value as malformed and close the Network Connection</i>
   *
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718064">3.8.1
   * SUBSCRIBE Fixed header</a>
   */
  public static final int SUBSCRIBE_FIXED_HEADER_BYTE = 0b10000010;

  // Byte positions in the SUBSCRIBE packet (counting after Fixed Header bytes)
  public static final int PACKET_IDENTIFIER_START_POSITION = 1;
  public static final int PACKET_IDENTIFIER_END_POSITION = 2;

  // SUBSCRIBE bit positions in the Requested QoS Byte
  public static final int REQUESTED_QOS_END_BIT = 1;
  public static final int REQUESTED_QOS_START_BIT = 0;

  // Variable Header
  private final int packetIdentifier;

  // Payload
  private final Map<QosLevel, String> topicsByRequestedQos;

  public SubscribePacket(
      FixedHeader fixedHeader,
      int packetIdentifier,
      Map<QosLevel, String> topicsByRequestedQos) {
    super(fixedHeader);
    this.packetIdentifier = packetIdentifier;
    this.topicsByRequestedQos = topicsByRequestedQos;
  }
}
