package com.jbroker.packet.decoder.impl;

import static com.jbroker.packet.PublishPacket.PACKET_IDENTIFIER_LENGTH;
import static com.jbroker.packet.PublishPacket.TOPIC_NAME_START_POSITION;
import static com.jbroker.packet.QosLevel.QOS_0;
import static com.jbroker.utils.ByteUtils.readByte;
import static com.jbroker.utils.PacketDecodeUtils.calculateStartBytePosition;
import static com.jbroker.utils.PacketDecodeUtils.combineBytesToInt;
import static com.jbroker.utils.PacketDecodeUtils.readTextField;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.PublishFixedHeader;
import com.jbroker.packet.PublishPacket;
import com.jbroker.packet.QosLevel;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import com.jbroker.utils.PacketDecodeUtils;

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
    return readTextField(packetBuffer, TOPIC_NAME_START_POSITION);
  }


  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718039">3.3.2.2
   * Packet Identifier</a>
   */
  private Integer readPacketIdentifier(byte[] packetBuffer, QosLevel qosLevel, String topicName) {
    // Packet Identifier in the PUBLISH packet is only present if the QoS level is 1 or 2
    if (qosLevel == QOS_0) {
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
    int applicationMessageEndBytePosition = remainingLength + 1;
    if (packetIdentifier != null) {
      applicationMessageStartBytePosition += PACKET_IDENTIFIER_LENGTH;
    }
    return PacketDecodeUtils.readApplicationMessage(
        packetBuffer,
        applicationMessageStartBytePosition,
        applicationMessageEndBytePosition
    );
  }
}
