package com.jbroker.packet;

import lombok.Getter;

@Getter
public class PublishPacket extends MqttPacket {

  // Byte positions in the PUBLISH packet (counting after Fixed Header bytes)
  public static final int TOPIC_NAME_START_POSITION = 1;
  public static final int PACKET_IDENTIFIER_LENGTH = 2;

  // Variable Header
  private final String topicName;
  private final Integer packetIdentifier; // Optional field, null if QoS level is 0

  // Payload
  private final String applicationMessage;

  public PublishPacket(
      PublishFixedHeader publishFixedHeader,
      String topicName,
      Integer packetIdentifier,
      String applicationMessage) {
    super(publishFixedHeader);
    this.topicName = topicName;
    this.packetIdentifier = packetIdentifier;
    this.applicationMessage = applicationMessage;
  }

  @Override
  public PublishFixedHeader getFixedHeader() {
    return ((PublishFixedHeader) super.getFixedHeader());
  }
}
