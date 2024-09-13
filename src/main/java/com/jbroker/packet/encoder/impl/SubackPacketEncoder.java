package com.jbroker.packet.encoder.impl;

import static com.jbroker.utils.PacketParseUtils.encodePacketIdentifier;

import com.jbroker.packet.SubackPacket;
import com.jbroker.packet.SubackPacket.SubackReturnCode;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import com.jbroker.utils.ArrayUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubackPacketEncoder implements MqttPacketEncoder<SubackPacket> {

  private final FixedHeaderEncoder fixedHeaderEncoder;

  @Override
  public byte[] encode(SubackPacket outboundPacket) {
    byte[] encodedFixedHeader = fixedHeaderEncoder.encode(outboundPacket.getFixedHeader());
    byte[] encodedVariableHeader = encodePacketIdentifier(outboundPacket.getPacketIdentifier());
    byte[] encodedPayload = encodeReturnCodes(outboundPacket.getReturnCodes());
    return ArrayUtils.mergeArrays(encodedFixedHeader, encodedVariableHeader, encodedPayload);
  }

  private byte[] encodeReturnCodes(List<SubackReturnCode> returnCodes) {
    byte[] encodedReturnCodes = new byte[returnCodes.size()];
    for (int i = 0; i < returnCodes.size(); i++) {
      encodedReturnCodes[i] = returnCodes.get(i).getCode();
    }
    return encodedReturnCodes;
  }
}
