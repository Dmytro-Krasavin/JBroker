package com.jbroker.packet.model.inbound.impl;

import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.model.AbstractMqttPacket;
import com.jbroker.packet.model.inbound.ClientToServerPacket;
import java.util.List;
import lombok.Getter;

@Getter
public class UnsubscribePacket extends AbstractMqttPacket implements ClientToServerPacket {

  /**
   * From MQTT specification: <i>Bits 3,2,1 and 0 of the fixed header of the UNSUBSCRIBE Control
   * Packet are reserved and MUST be set to 0,0,1 and 0 respectively. The Server MUST treat any
   * other value as malformed and close the Network Connection</i>
   *
   * @see <a
   * href="http://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html#_Toc398718073">3.10.1
   * UNSUBSCRIBE Fixed header</a>
   */
  public static final int UNSUBSCRIBE_FIXED_HEADER_BYTE = 0b10100010;

  // Byte positions in the UNSUBSCRIBE packet (counting after Fixed Header bytes)
  public static final int PACKET_IDENTIFIER_START_POSITION = 1;
  public static final int PACKET_IDENTIFIER_END_POSITION = 2;

  // Variable Header
  private final int packetIdentifier;

  // Payload
  private final List<String> topics;

  public UnsubscribePacket(FixedHeader fixedHeader, int packetIdentifier, List<String> topics) {
    super(fixedHeader);
    this.packetIdentifier = packetIdentifier;
    this.topics = topics;
  }
}
