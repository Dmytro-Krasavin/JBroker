package com.jbroker.packet;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QosLevel {

  QOS_0(0),   // At most once delivery
  QOS_1(1),   // At least once delivery
  QOS_2(2),   // Exactly once delivery
  ;

  private final int level;

  public static QosLevel resolveQoS(int qosLevel) {
    return Arrays.stream(values()).filter(type -> type.getLevel() == qosLevel)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
