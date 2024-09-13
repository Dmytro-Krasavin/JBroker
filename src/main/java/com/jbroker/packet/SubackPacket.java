package com.jbroker.packet;

import java.util.List;
import lombok.Getter;

@Getter
public class SubackPacket extends MqttPacket {

  // Byte positions in the SUBSCRIBE packet (counting after Fixed Header bytes)
  public static final int PACKET_IDENTIFIER_START_POSITION = 1;
  public static final int PACKET_IDENTIFIER_END_POSITION = 2;
  public static final int PACKET_IDENTIFIER_LENGTH = 2;

  // Variable Header
  private final int packetIdentifier;

  // Payload
  /**
   * From MQTT specification: <i>The payload contains a list of return codes. Each return code
   * corresponds to a Topic Filter in the SUBSCRIBE Packet being acknowledged. The order of return
   * codes in the SUBACK Packet MUST match the order of Topic Filters in the SUBSCRIBE Packet</i>
   *
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718071">3.9.3
   * SUBACK Payload</a>
   */
  private final List<SubackReturnCode> returnCodes;

  public SubackPacket(
      FixedHeader fixedHeader,
      int packetIdentifier,
      List<SubackReturnCode> returnCodes) {
    super(fixedHeader);
    this.packetIdentifier = packetIdentifier;
    this.returnCodes = returnCodes;
  }

  @Getter
  public enum SubackReturnCode {
    MAXIMUM_QOS_0((byte) 0x01),
    MAXIMUM_QOS_1((byte) 0x02),
    MAXIMUM_QOS_2((byte) 0x03),
    FAILURE((byte) 0x80),
    ;

    private final byte code;

    SubackReturnCode(byte code) {
      this.code = code;
    }
  }
}
