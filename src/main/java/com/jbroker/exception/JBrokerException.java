package com.jbroker.exception;

public class JBrokerException extends RuntimeException {

  public JBrokerException(String message) {
    super(message);
  }

  public JBrokerException(Throwable cause) {
    super(cause);
  }
}
