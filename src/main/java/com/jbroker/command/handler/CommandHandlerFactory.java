package com.jbroker.command.handler;

import static com.jbroker.command.CommandType.CONNECT;
import static com.jbroker.command.CommandType.DISCONNECT;
import static com.jbroker.command.CommandType.PINGREQ;
import static com.jbroker.command.CommandType.PUBLISH;
import static com.jbroker.command.CommandType.SUBSCRIBE;

import com.jbroker.command.CommandType;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.PublishHandler;
import com.jbroker.command.handler.impl.SubscribeHandler;
import com.jbroker.command.handler.impl.UnknownCommandHandler;
import com.jbroker.packet.MqttPacket;
import java.util.EnumMap;
import java.util.Map;

public class CommandHandlerFactory {

  private final Map<CommandType, CommandHandler<? extends MqttPacket, ? extends MqttPacket>> commandHandlerByCommandType;
  private final CommandHandler<? extends MqttPacket, ? extends MqttPacket> defaultHandler = new UnknownCommandHandler();

  private final ConnectHandler connectHandler;
  private final PingReqHandler pingReqHandler;
  private final PublishHandler publishHandler;
  private final SubscribeHandler subscribeHandler;
  private final DisconnectHandler disconnectHandler;

  public CommandHandlerFactory(
      ConnectHandler connectHandler,
      PingReqHandler pingReqHandler,
      PublishHandler publishHandler,
      SubscribeHandler subscribeHandler,
      DisconnectHandler disconnectHandler) {
    this.connectHandler = connectHandler;
    this.pingReqHandler = pingReqHandler;
    this.publishHandler = publishHandler;
    this.subscribeHandler = subscribeHandler;
    this.disconnectHandler = disconnectHandler;
    this.commandHandlerByCommandType = new EnumMap<>(CommandType.class);
    initializeMap();
  }

  public CommandHandler<? extends MqttPacket, ? extends MqttPacket> getCommandHandler(
      CommandType type) {
    return commandHandlerByCommandType.getOrDefault(type, defaultHandler);
  }

  private void initializeMap() {
    commandHandlerByCommandType.put(CONNECT, connectHandler);
    commandHandlerByCommandType.put(PINGREQ, pingReqHandler);
    commandHandlerByCommandType.put(PUBLISH, publishHandler);
    commandHandlerByCommandType.put(SUBSCRIBE, subscribeHandler);
    commandHandlerByCommandType.put(DISCONNECT, disconnectHandler);
  }
}
