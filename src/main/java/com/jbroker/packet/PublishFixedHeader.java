package com.jbroker.packet;

import static com.jbroker.command.CommandType.PUBLISH;

import lombok.Getter;

@Getter
public class PublishFixedHeader extends FixedHeader {

  // PUBLISH Fixed Header bit positions in first byte
  public static final int DUPLICATE_FLAG_BIT = 3;
  public static final int QOS_LEVEL_END_BIT = 2;
  public static final int QOS_LEVEL_START_BIT = 1;
  public static final int RETAIN_FLAG_BIT = 0;

  private final boolean duplicateFlag;
  private final QosLevel qosLevel;
  private final boolean retain; // TODO: implement retained feature

  public PublishFixedHeader(
      int remainingLength,
      boolean duplicateFlag,
      QosLevel qosLevel,
      boolean retain) {
    super(PUBLISH.getValue(), remainingLength);
    this.duplicateFlag = duplicateFlag;
    this.qosLevel = qosLevel;
    this.retain = retain;
  }
}
