package com.jbroker.packet.encoder.impl;

import static com.jbroker.packet.model.outbound.impl.ConnackPacket.CONNECT_ACKNOWLEDGE_FLAGS_POSITION;
import static com.jbroker.packet.model.outbound.impl.ConnackPacket.RETURN_CODE_POSITION;
import static com.jbroker.utils.ByteUtils.toArrayIndex;

import com.jbroker.packet.model.outbound.impl.ConnackPacket;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.MqttPacketEncoder;
import com.jbroker.utils.ArrayUtils;
import com.jbroker.utils.ByteUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnackPacketEncoder implements MqttPacketEncoder<ConnackPacket> {

  private final FixedHeaderEncoder fixedHeaderEncoder;

  @Override
  public byte[] encode(ConnackPacket outboundPacket) {
    byte[] encodedFixedHeader = fixedHeaderEncoder.encode(outboundPacket.getFixedHeader());
    byte[] encodedVariableHeader = getEncodedVariableHeader(outboundPacket);
    return ArrayUtils.mergeArrays(encodedFixedHeader, encodedVariableHeader);
  }

  private static byte[] getEncodedVariableHeader(ConnackPacket outboundPacket) {
    byte[] encodedVariableHeader = new byte[outboundPacket.getFixedHeader().getRemainingLength()];
    byte connectAcknowledgeFlags = ByteUtils.modifyBit(
        (byte) 0,
        ConnackPacket.SESSION_PRESENT_BIT,
        outboundPacket.isSessionPresent()
    );
    encodedVariableHeader[toArrayIndex(CONNECT_ACKNOWLEDGE_FLAGS_POSITION)]
        = connectAcknowledgeFlags;
    encodedVariableHeader[toArrayIndex(RETURN_CODE_POSITION)] = outboundPacket.getReturnCode();
    return encodedVariableHeader;
  }
}
