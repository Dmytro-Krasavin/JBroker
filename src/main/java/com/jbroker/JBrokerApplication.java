package com.jbroker;

import com.jbroker.client.ClientConnectionManager;
import com.jbroker.client.ClientConnectionRegistry;
import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.PublishHandler;
import com.jbroker.command.handler.impl.SubscribeHandler;
import com.jbroker.command.handler.impl.UnsubscribeHandler;
import com.jbroker.message.queue.impl.InMemoryMessageQueue;
import com.jbroker.message.MessagePublisher;
import com.jbroker.message.queue.MessageQueue;
import com.jbroker.message.queue.MessageQueueProcessor;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.PublishPacketDecoder;
import com.jbroker.packet.decoder.impl.SubscribePacketDecoder;
import com.jbroker.packet.decoder.impl.UnsubscribePacketDecoder;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.impl.ConnackPacketEncoder;
import com.jbroker.packet.encoder.impl.PingRespPacketEncoder;
import com.jbroker.packet.encoder.impl.PublishPacketEncoder;
import com.jbroker.packet.encoder.impl.SubackPacketEncoder;
import com.jbroker.packet.encoder.impl.UnsubackPacketEncoder;
import com.jbroker.packet.reader.FixedHeaderReader;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import com.jbroker.subscription.SubscriptionRegistry;

public class JBrokerApplication {

  private static final int BROKER_PORT = 1885;

  public static void main(String[] args) {
    SubscriptionRegistry subscriptionRegistry = new SubscriptionRegistry();
    ClientConnectionRegistry clientConnectionRegistry = new ClientConnectionRegistry();
    FixedHeaderEncoder fixedHeaderEncoder = new FixedHeaderEncoder();
    MessagePublisher messagePublisher = new MessagePublisher(
        subscriptionRegistry,
        clientConnectionRegistry
    );
    MessageQueue messageQueue = messageQueue();
    MessageQueueProcessor messageQueueProcessor = new MessageQueueProcessor(
        messageQueue,
        messagePublisher
    );
    Broker broker = new Broker(
        new ClientConnectionManager(
            new PacketReader(
                new FixedHeaderReader(),
                new ConnectPacketDecoder(),
                new PingReqPacketDecoder(),
                new PublishPacketDecoder(),
                new SubscribePacketDecoder(),
                new UnsubscribePacketDecoder(),
                new DisconnectPacketDecoder()
            ),
            new PacketWriter(
                new ConnackPacketEncoder(fixedHeaderEncoder),
                new PingRespPacketEncoder(fixedHeaderEncoder),
                new SubackPacketEncoder(fixedHeaderEncoder),
                new UnsubackPacketEncoder(fixedHeaderEncoder),
                new PublishPacketEncoder(fixedHeaderEncoder)
            ),
            new CommandDispatcher(
                new CommandHandlerFactory(
                    new ConnectHandler(),
                    new PingReqHandler(),
                    new PublishHandler(messageQueue),
                    new SubscribeHandler(subscriptionRegistry),
                    new UnsubscribeHandler(subscriptionRegistry),
                    new DisconnectHandler()
                )
            ),
            clientConnectionRegistry
        ),
        messageQueueProcessor
    );
    broker.run(BROKER_PORT);
  }

  private static MessageQueue messageQueue() {
    return new InMemoryMessageQueue();
  }
}
