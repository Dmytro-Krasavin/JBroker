package com.jbroker.packet;

import lombok.Getter;

@Getter
public class PublishFixedHeader extends FixedHeader {

  // PUBLISH Fixed Header bit positions in first byte
  public static final int DUPLICATE_FLAG_BIT = 3;
  public static final int QOS_LEVEL_END_BIT = 2;
  public static final int QOS_LEVEL_START_BIT = 1;
  public static final int RETAIN_FLAG_BIT = 0;

  private final boolean duplicateFlag;
  private final int qosLevel;
  private final boolean retain;

  public PublishFixedHeader(
      int controlPacketType,
      int remainingLength,
      boolean duplicateFlag,
      int qosLevel,
      boolean retain) {
    super(controlPacketType, remainingLength);
    this.duplicateFlag = duplicateFlag;
    this.qosLevel = qosLevel;
    this.retain = retain;
  }
}
