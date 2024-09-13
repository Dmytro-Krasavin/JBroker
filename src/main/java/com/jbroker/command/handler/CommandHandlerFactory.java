package com.jbroker.command.handler;

import static com.jbroker.command.CommandType.CONNECT;
import static com.jbroker.command.CommandType.DISCONNECT;
import static com.jbroker.command.CommandType.PINGREQ;
import static com.jbroker.command.CommandType.PUBLISH;
import static com.jbroker.command.CommandType.SUBSCRIBE;
import static com.jbroker.command.CommandType.UNSUBSCRIBE;

import com.jbroker.command.CommandType;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.PublishHandler;
import com.jbroker.command.handler.impl.SubscribeHandler;
import com.jbroker.command.handler.impl.UnknownCommandHandler;
import com.jbroker.command.handler.impl.UnsubscribeHandler;
import com.jbroker.packet.MqttPacket;
import java.util.EnumMap;
import java.util.Map;

public class CommandHandlerFactory {

  private final Map<CommandType, CommandHandler<? extends MqttPacket, ? extends MqttPacket>> commandHandlerByCommandType;
  private final CommandHandler<MqttPacket, MqttPacket> defaultHandler = new UnknownCommandHandler();

  private final ConnectHandler connectHandler;
  private final PingReqHandler pingReqHandler;
  private final PublishHandler publishHandler;
  private final SubscribeHandler subscribeHandler;
  private final UnsubscribeHandler unsubscribeHandler;
  private final DisconnectHandler disconnectHandler;

  public CommandHandlerFactory(
      ConnectHandler connectHandler,
      PingReqHandler pingReqHandler,
      PublishHandler publishHandler,
      SubscribeHandler subscribeHandler,
      UnsubscribeHandler unsubscribeHandler,
      DisconnectHandler disconnectHandler) {
    this.connectHandler = connectHandler;
    this.pingReqHandler = pingReqHandler;
    this.publishHandler = publishHandler;
    this.subscribeHandler = subscribeHandler;
    this.unsubscribeHandler = unsubscribeHandler;
    this.disconnectHandler = disconnectHandler;
    this.commandHandlerByCommandType = new EnumMap<>(CommandType.class);
    initializeMap();
  }

  @SuppressWarnings("unchecked")
  public <InboundPacket extends MqttPacket, OutboundPacket extends MqttPacket> CommandHandler<InboundPacket, OutboundPacket> getCommandHandler(
      CommandType type) {
    return (CommandHandler<InboundPacket, OutboundPacket>) commandHandlerByCommandType.getOrDefault(
        type, defaultHandler
    );
  }

  private void initializeMap() {
    commandHandlerByCommandType.put(CONNECT, connectHandler);
    commandHandlerByCommandType.put(PINGREQ, pingReqHandler);
    commandHandlerByCommandType.put(PUBLISH, publishHandler);
    commandHandlerByCommandType.put(SUBSCRIBE, subscribeHandler);
    commandHandlerByCommandType.put(UNSUBSCRIBE, unsubscribeHandler);
    commandHandlerByCommandType.put(DISCONNECT, disconnectHandler);
  }
}
