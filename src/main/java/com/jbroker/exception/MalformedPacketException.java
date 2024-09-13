package com.jbroker.exception;

import com.jbroker.command.CommandType;

public class MalformedPacketException extends JBrokerException {

  private final CommandType commandType;

  public MalformedPacketException(CommandType commandType, String message) {
    super(message);
    this.commandType = commandType;
  }
}
