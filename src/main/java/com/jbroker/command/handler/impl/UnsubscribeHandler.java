package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.UNSUBACK;
import static com.jbroker.packet.model.outbound.impl.UnsubackPacket.UNSUBACK_REMAINING_LENGTH;

import com.jbroker.command.handler.AbstractCommandHandler;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.inbound.impl.UnsubscribePacket;
import com.jbroker.packet.model.outbound.impl.UnsubackPacket;
import com.jbroker.subscription.registry.SubscriptionRegistry;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UnsubscribeHandler extends AbstractCommandHandler<UnsubscribePacket, UnsubackPacket> {

  private final SubscriptionRegistry subscriptionRegistry;

  @Override
  protected UnsubackPacket getOutboundPacket(UnsubscribePacket unsubscribePacket) {
    log.info("Unsubscribe topics: {}", Arrays.toString(unsubscribePacket.getTopics().toArray()));
    log.debug("Packet Identifier: {}", unsubscribePacket.getPacketIdentifier());
    FixedHeader fixedHeader = new FixedHeader(UNSUBACK.getValue(), UNSUBACK_REMAINING_LENGTH);
    return new UnsubackPacket(fixedHeader, unsubscribePacket.getPacketIdentifier());
  }

  @Override
  protected void doSideEffects(UnsubscribePacket unsubscribePacket, String clientId) {
    unsubscribePacket.getTopics()
        .forEach(topic -> subscriptionRegistry.unsubscribe(topic, clientId));
  }
}
