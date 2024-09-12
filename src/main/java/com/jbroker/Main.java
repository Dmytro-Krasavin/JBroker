package com.jbroker;

import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.reader.FixedHeaderReader;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;

public class Main {

  private static final int BROKER_PORT = 1885;

  public static void main(String[] args) {
    Broker broker = new Broker(
        new ClientHandlerFactory(
            new PacketReader(
                new FixedHeaderReader(),
                new ConnectPacketDecoder(),
                new PingReqPacketDecoder(),
                new DisconnectPacketDecoder()
            )
        )
    );
    broker.run(BROKER_PORT);
  }
}
