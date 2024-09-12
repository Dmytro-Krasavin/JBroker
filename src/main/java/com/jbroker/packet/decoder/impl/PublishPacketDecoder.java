package com.jbroker.packet.decoder.impl;

import static com.jbroker.packet.PublishPacket.PACKET_IDENTIFIER_LENGTH;
import static com.jbroker.packet.PublishPacket.TOPIC_NAME_START_POSITION;
import static com.jbroker.packet.QosLevel.QOS_0;
import static com.jbroker.utils.ByteUtils.readByte;
import static com.jbroker.utils.PacketParseUtils.calculateStartBytePosition;
import static com.jbroker.utils.PacketParseUtils.combineBytesToInt;
import static com.jbroker.utils.PacketParseUtils.readStringField;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PublishFixedHeader;
import com.jbroker.packet.PublishPacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import com.jbroker.utils.PacketParseUtils;

public class PublishPacketDecoder implements MqttPacketDecoder<PublishPacket> {

  @Override
  public PublishPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    if (fixedHeader instanceof PublishFixedHeader publishFixedHeader) {
      String topicName = readTopicName(packetBuffer);
      Integer packetIdentifier = readPacketIdentifier(
          packetBuffer,
          publishFixedHeader.getQosLevel(),
          topicName
      );
      String applicationMessage = readApplicationMessage(
          packetBuffer,
          topicName,
          packetIdentifier,
          publishFixedHeader.getRemainingLength()
      );
      return new PublishPacket(publishFixedHeader, topicName, packetIdentifier, applicationMessage);
    }
    throw new IllegalStateException(
        "Fixed Header for PUBLISH packet should be instance of PublishFixedHeader");
  }

  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718039">3.3.2.1
   * Topic Name</a>
   */
  private String readTopicName(byte[] packetBuffer) {
    return readStringField(packetBuffer, TOPIC_NAME_START_POSITION);
  }


  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718039">3.3.2.2
   * Packet Identifier</a>
   */
  private Integer readPacketIdentifier(byte[] packetBuffer, int qosLevel, String topicName) {
    if (qosLevel == QOS_0.getLevel()) {
      return null;
    }

    int packetIdentifierStartBytePosition = calculateStartBytePosition(
        TOPIC_NAME_START_POSITION, topicName
    );
    int packetIdentifierEndBytePosition = packetIdentifierStartBytePosition + 1;
    byte packetIdentifierMSB = readByte(packetBuffer, packetIdentifierStartBytePosition);
    byte packetIdentifierLSB = readByte(packetBuffer, packetIdentifierEndBytePosition);
    return combineBytesToInt(packetIdentifierMSB, packetIdentifierLSB);
  }

  /**
   * @see <a
   * href="https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718040">3.3.3
   * Payload</a>
   */
  private static String readApplicationMessage(
      byte[] packetBuffer,
      String topicName,
      Integer packetIdentifier,
      int remainingLength) {
    int applicationMessageStartBytePosition = calculateStartBytePosition(
        TOPIC_NAME_START_POSITION, topicName
    );
    if (packetIdentifier != null) {
      applicationMessageStartBytePosition += PACKET_IDENTIFIER_LENGTH;
    }
    return PacketParseUtils.readApplicationMessage(
        packetBuffer,
        applicationMessageStartBytePosition,
        remainingLength
    );
  }
}
