package com.jbroker;

import com.jbroker.client.ClientHandlerFactory;
import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.PublishHandler;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.PublishPacketDecoder;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.impl.ConnackPacketEncoder;
import com.jbroker.packet.encoder.impl.PingRespPacketEncoder;
import com.jbroker.packet.reader.FixedHeaderReader;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;

public class Main {

  private static final int BROKER_PORT = 1885;

  public static void main(String[] args) {
    FixedHeaderEncoder fixedHeaderEncoder = new FixedHeaderEncoder();
    Broker broker = new Broker(
        new ClientHandlerFactory(
            new PacketReader(
                new FixedHeaderReader(),
                new ConnectPacketDecoder(),
                new PingReqPacketDecoder(),
                new PublishPacketDecoder(),
                new DisconnectPacketDecoder()
            ),
            new PacketWriter(
                new ConnackPacketEncoder(fixedHeaderEncoder),
                new PingRespPacketEncoder(fixedHeaderEncoder)
            ),
            new CommandDispatcher(
                new CommandHandlerFactory(
                    new ConnectHandler(),
                    new PingReqHandler(),
                    new PublishHandler(),
                    new DisconnectHandler()
                )
            )
        )
    );
    broker.run(BROKER_PORT);
  }
}
