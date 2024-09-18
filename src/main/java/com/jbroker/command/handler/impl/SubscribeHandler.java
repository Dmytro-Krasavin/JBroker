package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.SUBACK;
import static com.jbroker.packet.model.outbound.impl.SubackPacket.PACKET_IDENTIFIER_LENGTH;

import com.jbroker.command.handler.AbstractCommandHandler;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.impl.SubscribePacket;
import com.jbroker.packet.model.outbound.impl.SubackPacket;
import com.jbroker.packet.model.outbound.impl.SubackPacket.SubackReturnCode;
import com.jbroker.subscription.Subscriber;
import com.jbroker.subscription.registry.SubscriptionRegistry;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SubscribeHandler extends AbstractCommandHandler<SubscribePacket, SubackPacket> {

  private final SubscriptionRegistry subscriptionRegistry;

  @Override
  protected SubackPacket getOutboundPacket(SubscribePacket subscribePacket) {
    log.info("Topics by requested QoS level: {}", subscribePacket.getTopicsByRequestedQos());
    log.debug("Packet Identifier: {}", subscribePacket.getPacketIdentifier());

    // TODO: implement correct return code, using requested QoS level
    List<SubackReturnCode> returnCodes = new LinkedList<>();
    subscribePacket.getTopicsByRequestedQos().forEach((qosLevel, topic) ->
        returnCodes.add(SubackReturnCode.MAXIMUM_QOS_0) // Supports only QoS 0 for now
    );

    int subackRemainingLength = PACKET_IDENTIFIER_LENGTH + returnCodes.size();
    FixedHeader fixedHeader = new FixedHeader(SUBACK.getValue(), subackRemainingLength);
    return new SubackPacket(fixedHeader, subscribePacket.getPacketIdentifier(), returnCodes);
  }

  @Override
  protected void doSideEffects(SubscribePacket subscribePacket, String clientId) {
    subscribePacket.getTopicsByRequestedQos().forEach((qosLevel, topic) ->
        subscriptionRegistry.subscribe(topic, new Subscriber(clientId, qosLevel)));
  }
}
