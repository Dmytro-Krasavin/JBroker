package com.jbroker.packet.encoder.impl;

import static com.jbroker.packet.ConnackPacket.CONNACK_REMAINING_LENGTH;
import static com.jbroker.packet.ConnackPacket.CONNECT_ACKNOWLEDGE_FLAGS_POSITION;
import static com.jbroker.packet.ConnackPacket.RETURN_CODE_POSITION;
import static com.jbroker.utils.ByteUtils.toArrayIndex;

import com.jbroker.command.CommandType;
import com.jbroker.packet.ConnackPacket;
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
    byte[] encodedFixedHeader = fixedHeaderEncoder.encode(
        CommandType.CONNACK,
        ConnackPacket.CONNACK_REMAINING_LENGTH
    );

    byte[] encodedVariableHeader = new byte[CONNACK_REMAINING_LENGTH];
    byte connectAcknowledgeFlags = ByteUtils.modifyBit(
        (byte) 0,
        ConnackPacket.SESSION_PRESENT_BIT,
        outboundPacket.isSessionPresent()
    );
    encodedVariableHeader[toArrayIndex(CONNECT_ACKNOWLEDGE_FLAGS_POSITION)]
        = connectAcknowledgeFlags;
    encodedVariableHeader[toArrayIndex(RETURN_CODE_POSITION)] = outboundPacket.getReturnCode();

    return ArrayUtils.mergeArrays(encodedFixedHeader, encodedVariableHeader);
  }
}
