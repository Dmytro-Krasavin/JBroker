package com.jbroker.exception;

import com.jbroker.command.CommandType;
import lombok.Getter;

@Getter
public class PacketSendFailedException extends RuntimeException {

  private final CommandType commandType;

  public PacketSendFailedException(CommandType commandType, Throwable cause) {
    super(cause);
    this.commandType = commandType;
  }
}
