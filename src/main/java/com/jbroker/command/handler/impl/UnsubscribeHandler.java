package com.jbroker.command.handler.impl;

import static com.jbroker.command.CommandType.UNSUBACK;
import static com.jbroker.packet.UnsubackPacket.UNSUBACK_REMAINING_LENGTH;

import com.jbroker.command.handler.CommandHandler;
import com.jbroker.packet.FixedHeader;
import com.jbroker.packet.UnsubackPacket;
import com.jbroker.packet.UnsubscribePacket;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnsubscribeHandler implements CommandHandler<UnsubscribePacket, UnsubackPacket> {

  @Override
  public Optional<UnsubackPacket> handleCommand(UnsubscribePacket unsubscribePacket) {
    log.info("Packet Identifier: {}", unsubscribePacket.getPacketIdentifier());
    FixedHeader fixedHeader = new FixedHeader(UNSUBACK.getValue(), UNSUBACK_REMAINING_LENGTH);
    return Optional.of(
        new UnsubackPacket(fixedHeader, unsubscribePacket.getPacketIdentifier())
    );
  }
}
