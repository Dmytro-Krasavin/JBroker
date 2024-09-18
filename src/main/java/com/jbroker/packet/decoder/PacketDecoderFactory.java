package com.jbroker.packet.decoder;

import com.jbroker.command.CommandType;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.PublishPacketDecoder;
import com.jbroker.packet.decoder.impl.SubscribePacketDecoder;
import com.jbroker.packet.decoder.impl.UnsubscribePacketDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PacketDecoderFactory {

  private final ConnectPacketDecoder connectDecoder;
  private final PingReqPacketDecoder pingReqDecoder;
  private final PublishPacketDecoder publishDecoder;
  private final SubscribePacketDecoder subscribeDecoder;
  private final UnsubscribePacketDecoder unsubscribeDecoder;
  private final DisconnectPacketDecoder disconnectDecoder;

  public MqttPacketDecoder getPacketDecoder(CommandType commandType) {
    return switch (commandType) {
      case CONNECT -> connectDecoder;
      case PINGREQ -> pingReqDecoder;
      case PUBLISH -> publishDecoder;
      case SUBSCRIBE -> subscribeDecoder;
      case UNSUBSCRIBE -> unsubscribeDecoder;
      case DISCONNECT -> disconnectDecoder;
      default -> throw new IllegalArgumentException(
          "Could not find applicable packet encoder for command type: " + commandType.name());
    };
  }
}
