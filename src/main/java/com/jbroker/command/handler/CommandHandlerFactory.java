package com.jbroker.command.handler;

import static com.jbroker.command.CommandType.CONNECT;
import static com.jbroker.command.CommandType.DISCONNECT;
import static com.jbroker.command.CommandType.PINGREQ;

import com.jbroker.command.CommandType;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.UnknownCommandHandler;
import com.jbroker.packet.MqttPacket;
import java.util.EnumMap;
import java.util.Map;

public class CommandHandlerFactory {

  private final Map<CommandType, CommandHandler<? extends MqttPacket, ? extends MqttPacket>> commandHandlerByCommandType;
  private final CommandHandler<? extends MqttPacket, ? extends MqttPacket> defaultHandler = new UnknownCommandHandler();

  private final ConnectHandler connectHandler;
  private final PingReqHandler pingReqHandler;
  private final DisconnectHandler disconnectHandler;

  public CommandHandlerFactory(
      ConnectHandler connectHandler,
      PingReqHandler pingReqHandler,
      DisconnectHandler disconnectHandler) {
    this.connectHandler = connectHandler;
    this.pingReqHandler = pingReqHandler;
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
    commandHandlerByCommandType.put(DISCONNECT, disconnectHandler);
  }
}
