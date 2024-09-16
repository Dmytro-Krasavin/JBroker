package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.UNSUBACK;
import static com.jbroker.packet.model.outbound.impl.UnsubackPacket.UNSUBACK_REMAINING_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.outbound.impl.UnsubackPacket;
import com.jbroker.packet.model.inbound.impl.UnsubscribePacket;
import com.jbroker.subscription.SubscriptionRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UnsubscribeHandler implements CommandHandler<UnsubscribePacket, UnsubackPacket> {

  private final SubscriptionRegistry subscriptionRegistry;

  @Override
  public Optional<UnsubackPacket> handleCommand(
      UnsubscribePacket unsubscribePacket,
      String clientId) {
    List<String> topics = unsubscribePacket.getTopics();
    log.info("Unsubscribe topics: {}", Arrays.toString(topics.toArray()));
    log.info("Packet Identifier: {}", unsubscribePacket.getPacketIdentifier());
    FixedHeader fixedHeader = new FixedHeader(UNSUBACK.getValue(), UNSUBACK_REMAINING_LENGTH);
    Optional<UnsubackPacket> unsubackPacket = Optional.of(
        new UnsubackPacket(fixedHeader, unsubscribePacket.getPacketIdentifier())
    );

    topics.forEach(topic -> subscriptionRegistry.unsubscribe(topic, clientId));

    return unsubackPacket;
  }
}
