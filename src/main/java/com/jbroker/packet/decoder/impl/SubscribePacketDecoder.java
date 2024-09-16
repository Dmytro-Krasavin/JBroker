package com.jbroker.packet.decoder.impl;

import static com.jbroker.packet.model.inbound.impl.SubscribePacket.PACKET_IDENTIFIER_END_POSITION;
import static com.jbroker.packet.model.inbound.impl.SubscribePacket.REQUESTED_QOS_END_BIT;
import static com.jbroker.packet.model.inbound.impl.SubscribePacket.REQUESTED_QOS_START_BIT;
import static com.jbroker.utils.ByteUtils.combineBits;
import static com.jbroker.utils.ByteUtils.readByte;
import static com.jbroker.utils.PacketDecodeUtils.calculateStartBytePosition;
import static com.jbroker.utils.PacketDecodeUtils.decodePacketIdentifier;
import static com.jbroker.utils.PacketDecodeUtils.readTextField;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.QosLevel;
import com.jbroker.packet.model.inbound.impl.SubscribePacket;
import com.jbroker.packet.model.inbound.impl.UnsubscribePacket;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class SubscribePacketDecoder implements MqttPacketDecoder {

  @Override
  public SubscribePacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    int packetIdentifier = readPacketIdentifier(packetBuffer);
    Map<QosLevel, String> topicsByRequestedQos = readTopicsByRequestedQos(packetBuffer);
    return new SubscribePacket(fixedHeader, packetIdentifier, topicsByRequestedQos);
  }

  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718065">SUBSCRIBE
   * 3.8.2 Variable header</a>
   */
  private int readPacketIdentifier(byte[] packetBuffer) {
    return decodePacketIdentifier(packetBuffer, UnsubscribePacket.PACKET_IDENTIFIER_START_POSITION);
  }

  /**
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718066">SUBSCRIBE
   * 3.8.3 Payload</a>
   */
  private static Map<QosLevel, String> readTopicsByRequestedQos(byte[] packetBuffer) {
    Map<QosLevel, String> topicsByQoS = new LinkedHashMap<>();
    int currentPosition = PACKET_IDENTIFIER_END_POSITION + 1;
    while (currentPosition < packetBuffer.length) {
      String topicFilter = readTextField(packetBuffer, currentPosition);

      currentPosition = calculateStartBytePosition(currentPosition, topicFilter);
      byte requestedQosByte = readByte(packetBuffer, currentPosition);
      QosLevel requestedQoS = QosLevel.resolveQoS(combineBits(
          requestedQosByte,
          REQUESTED_QOS_START_BIT,
          REQUESTED_QOS_END_BIT
      ));

      topicsByQoS.put(requestedQoS, topicFilter);
      currentPosition++;
    }
    return topicsByQoS;
  }
}
