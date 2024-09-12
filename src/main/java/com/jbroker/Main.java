package com.jbroker;

import com.jbroker.packet.parser.ConnectParser;
import com.jbroker.packet.parser.DisconnectParser;
import com.jbroker.packet.parser.FixedHeaderParser;
import com.jbroker.packet.parser.PacketParserDispatcher;
import com.jbroker.packet.parser.PingReqParser;

public class Main {

  private static final int BROKER_PORT = 1885;

  public static void main(String[] args) {
    Broker broker = new Broker(
        new ClientHandlerFactory(
            new PacketParserDispatcher(
                new FixedHeaderParser(),
                new ConnectParser(),
                new PingReqParser(),
                new DisconnectParser()
            )
        )
    );
    broker.run(BROKER_PORT);
  }
}
