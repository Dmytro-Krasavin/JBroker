package com.jbroker.packet.decoder.impl;

import static com.jbroker.packet.UnsubscribePacket.PACKET_IDENTIFIER_END_POSITION;
import static com.jbroker.packet.UnsubscribePacket.PACKET_IDENTIFIER_START_POSITION;
import static com.jbroker.utils.PacketDecodeUtils.calculateStartBytePosition;
import static com.jbroker.utils.PacketDecodeUtils.decodePacketIdentifier;
import static com.jbroker.utils.PacketDecodeUtils.readStringField;

import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.UnsubscribePacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import java.util.LinkedList;
import java.util.List;

public class UnsubscribePacketDecoder implements MqttPacketDecoder<UnsubscribePacket> {

  @Override
  public UnsubscribePacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    int packetIdentifier = readPacketIdentifier(packetBuffer);
    List<String> topics = readTopics(packetBuffer);
    return new UnsubscribePacket(fixedHeader, packetIdentifier, topics);
  }

  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718065">SUBSCRIBE
   * 3.8.2 Variable header</a>
   */
  private int readPacketIdentifier(byte[] packetBuffer) {
    return decodePacketIdentifier(packetBuffer, PACKET_IDENTIFIER_START_POSITION);
  }

  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718066">SUBSCRIBE
   * 3.8.3 Payload</a>
   */
  private static List<String> readTopics(byte[] packetBuffer) {
    List<String> topics = new LinkedList<>();
    int currentPosition = PACKET_IDENTIFIER_END_POSITION + 1;
    while (currentPosition < packetBuffer.length) {
      String topicFilter = readStringField(packetBuffer, currentPosition);
      topics.add(topicFilter);
      currentPosition = calculateStartBytePosition(currentPosition, topicFilter);
    }
    return topics;
  }
}
