package com.jbroker.packet.decoder.impl;

import static com.jbroker.packet.model.inbound.impl.ConnectPacket.CLEAN_SESSION_FLAG_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.CLIENT_ID_START_POSITION;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.CONNECT_FLAGS_POSITION;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.KEEP_ALIVE_END_POSITION;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.KEEP_ALIVE_START_POSITION;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.PASSWORD_FLAG_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.PROTOCOL_LEVEL_POSITION;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.USERNAME_FLAG_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.WILL_FLAG_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.WILL_QOS_END_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.WILL_QOS_START_BIT;
import static com.jbroker.packet.model.inbound.impl.ConnectPacket.WILL_RETAIN_FLAG_BIT;
import static com.jbroker.utils.ByteUtils.readByte;
import static com.jbroker.utils.PacketDecodeUtils.calculateStartBytePosition;
import static com.jbroker.utils.PacketDecodeUtils.combineBytesToInt;
import static com.jbroker.utils.PacketDecodeUtils.readTextField;

import com.jbroker.packet.model.inbound.impl.ConnectPacket;
import com.jbroker.packet.model.header.FixedHeader;
import com.jbroker.packet.decoder.MqttPacketDecoder;
import com.jbroker.utils.ByteUtils;

public class ConnectPacketDecoder implements MqttPacketDecoder {

  @Override
  public ConnectPacket decode(FixedHeader fixedHeader, byte[] packetBuffer) {
    String protocolName = readProtocolName(packetBuffer);
    byte protocolLevel = readByte(packetBuffer, PROTOCOL_LEVEL_POSITION);

    // Read Connect Flags
    byte connectFlags = readByte(packetBuffer, CONNECT_FLAGS_POSITION);
    boolean userNameFlag = ByteUtils.isBitSet(connectFlags, USERNAME_FLAG_BIT);
    boolean passwordFlag = ByteUtils.isBitSet(connectFlags, PASSWORD_FLAG_BIT)
        && userNameFlag; // See https://docs.oasis-open.org/mqtt/mqtt/v3.1.1/os/mqtt-v3.1.1-os.html 3.1.2.9 Password Flag
    boolean willFlag = ByteUtils.isBitSet(connectFlags, WILL_FLAG_BIT);
    boolean willRetain = ByteUtils.isBitSet(connectFlags, WILL_RETAIN_FLAG_BIT) && willFlag;
    int willQoS = willFlag
        ? ByteUtils.combineBits(connectFlags, WILL_QOS_START_BIT, WILL_QOS_END_BIT)
        : 0;
    boolean cleanSession = ByteUtils.isBitSet(connectFlags, CLEAN_SESSION_FLAG_BIT);

    int keepAlive = readKeepAlive(packetBuffer);

    String clientId = readClientId(packetBuffer);

    // Read Optional Fields (willTopic, willMessage, userName, password)
    String willTopic = willFlag
        ? readWillTopic(packetBuffer, clientId)
        : null;
    String willMessage = willFlag
        ? readWillMessage(packetBuffer, clientId, willTopic)
        : null;
    String userName = userNameFlag
        ? readUserName(packetBuffer, clientId, willTopic, willMessage)
        : null;
    String password = passwordFlag
        ? readPassword(packetBuffer, clientId, willTopic, willMessage, userName)
        : null;

    return ConnectPacket.builder()
        .fixedHeader(fixedHeader)
        .protocolName(protocolName)
        .protocolLevel(protocolLevel)
        .userNameFlag(userNameFlag)
        .passwordFlag(passwordFlag)
        .willRetain(willRetain)
        .willQoS(willQoS)
        .willFlag(willFlag)
        .cleanSession(cleanSession)
        .keepAlive(keepAlive)
        .clientId(clientId)
        .willTopic(willTopic)
        .willMessage(willMessage)
        .userName(userName)
        .password(password)
        .build();
  }

  private String readProtocolName(byte[] packetBuffer) {
    return readTextField(packetBuffer, ConnectPacket.PROTOCOL_NAME_START_POSITION);
  }

  private int readKeepAlive(byte[] packetBuffer) {
    byte keepAliveMSB = readByte(packetBuffer, KEEP_ALIVE_START_POSITION);
    byte keepAliveLSB = readByte(packetBuffer, KEEP_ALIVE_END_POSITION);
    return combineBytesToInt(keepAliveMSB, keepAliveLSB);
  }

  private String readClientId(byte[] packetBuffer) {
    return readTextField(packetBuffer, ConnectPacket.CLIENT_ID_START_POSITION);
  }

  private String readWillTopic(byte[] packetBuffer, String clientId) {
    int willTopicStartPosition = calculateStartBytePosition(CLIENT_ID_START_POSITION, clientId);
    return readTextField(packetBuffer, willTopicStartPosition);
  }

  private String readWillMessage(byte[] packetBuffer, String clientId, String willTopic) {
    int willMessageStartPosition = calculateStartBytePosition(
        CLIENT_ID_START_POSITION, clientId, willTopic);
    return readTextField(packetBuffer, willMessageStartPosition);
  }

  private String readUserName(
      byte[] packetBuffer,
      String clientId,
      String willTopic,
      String willMessage) {
    int userNameStartBytePosition = calculateStartBytePosition(
        CLIENT_ID_START_POSITION, clientId, willTopic, willMessage
    );
    return readTextField(packetBuffer, userNameStartBytePosition);
  }

  private String readPassword(
      byte[] packetBuffer,
      String clientId,
      String willTopic,
      String willMessage,
      String userName) {
    int passwordStartPosition = calculateStartBytePosition(
        CLIENT_ID_START_POSITION, clientId, willTopic, willMessage, userName
    );
    return readTextField(packetBuffer, passwordStartPosition);
  }
}
