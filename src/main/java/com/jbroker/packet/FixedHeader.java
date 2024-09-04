package com.jbroker.packet;

public record FixedHeader(
    int controlPacketType,
    int remainingLength) {

}
