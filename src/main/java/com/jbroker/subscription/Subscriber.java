package com.jbroker.subscription;

import com.jbroker.packet.QosLevel;

public record Subscriber(
    String clientId,
    QosLevel qosLevel) {

}
