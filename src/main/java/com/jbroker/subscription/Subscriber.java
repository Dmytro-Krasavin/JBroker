package com.jbroker.subscription;

import com.jbroker.packet.model.QosLevel;

public record Subscriber(
    String clientId,
    QosLevel qosLevel) {

}
