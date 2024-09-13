package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.SUBACK;
import static com.jbroker.packet.SubackPacket.PACKET_IDENTIFIER_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.SubackPacket;
import com.jbroker.packet.SubackPacket.SubackReturnCode;
import com.jbroker.packet.SubscribePacket;
import com.jbroker.subscription.Subscriber;
import com.jbroker.subscription.SubscriptionRegistry;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SubscribeHandler implements CommandHandler<SubscribePacket, SubackPacket> {

  private final SubscriptionRegistry subscriptionRegistry;

  @Override
  public Optional<SubackPacket> handleCommand(SubscribePacket subscribePacket, String clientId) {
    log.info("Packet Identifier: {}", subscribePacket.getPacketIdentifier());
    log.info("Topics by requested QoS level: {}", subscribePacket.getTopicsByRequestedQos());

    // TODO: implement correct return code, using requested QoS level
    List<SubackReturnCode> returnCodes = new LinkedList<>();
    subscribePacket.getTopicsByRequestedQos().forEach((qosLevel, topic) ->
        returnCodes.add(SubackReturnCode.MAXIMUM_QOS_0) // Supports only QoS 0 for now
    );

    int subackRemainingLength = PACKET_IDENTIFIER_LENGTH + returnCodes.size();
    FixedHeader fixedHeader = new FixedHeader(SUBACK.getValue(), subackRemainingLength);
    Optional<SubackPacket> subackPacket = Optional.of(
        new SubackPacket(fixedHeader, subscribePacket.getPacketIdentifier(), returnCodes)
    );

    subscribePacket.getTopicsByRequestedQos().forEach((qosLevel, topic) ->
        subscriptionRegistry.subscribe(topic, new Subscriber(clientId, qosLevel)));

    return subackPacket;
  }
}
