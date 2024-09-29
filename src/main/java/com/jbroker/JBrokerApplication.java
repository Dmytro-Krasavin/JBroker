package com.jbroker;

import com.jbroker.command.CommandDispatcher;
import com.jbroker.command.handler.CommandHandlerFactory;
import com.jbroker.command.handler.impl.ConnectHandler;
import com.jbroker.command.handler.impl.DisconnectHandler;
import com.jbroker.command.handler.impl.PingReqHandler;
import com.jbroker.command.handler.impl.PublishHandler;
import com.jbroker.command.handler.impl.SubscribeHandler;
import com.jbroker.command.handler.impl.UnsubscribeHandler;
import com.jbroker.connection.ClientConnectionManager;
import com.jbroker.connection.registry.ClientConnectionRegistry;
import com.jbroker.connection.registry.impl.InMemoryClientConnectionRegistry;
import com.jbroker.message.MessagePublisher;
import com.jbroker.message.queue.MessageQueue;
import com.jbroker.message.queue.MessageQueueProcessor;
import com.jbroker.message.queue.impl.InMemoryMessageQueue;
import com.jbroker.message.topic.TopicFilter;
import com.jbroker.packet.decoder.PacketDecoderFactory;
import com.jbroker.packet.decoder.impl.ConnectPacketDecoder;
import com.jbroker.packet.decoder.impl.DisconnectPacketDecoder;
import com.jbroker.packet.decoder.impl.PingReqPacketDecoder;
import com.jbroker.packet.decoder.impl.PublishPacketDecoder;
import com.jbroker.packet.decoder.impl.SubscribePacketDecoder;
import com.jbroker.packet.decoder.impl.UnsubscribePacketDecoder;
import com.jbroker.packet.encoder.FixedHeaderEncoder;
import com.jbroker.packet.encoder.PacketEncoderFactory;
import com.jbroker.packet.encoder.impl.ConnackPacketEncoder;
import com.jbroker.packet.encoder.impl.PingRespPacketEncoder;
import com.jbroker.packet.encoder.impl.PublishPacketEncoder;
import com.jbroker.packet.encoder.impl.SubackPacketEncoder;
import com.jbroker.packet.encoder.impl.UnsubackPacketEncoder;
import com.jbroker.packet.reader.FixedHeaderReader;
import com.jbroker.packet.reader.PacketReader;
import com.jbroker.packet.writer.PacketWriter;
import com.jbroker.subscription.registry.SubscriptionRegistry;
import com.jbroker.subscription.registry.impl.InMemorySubscriptionRegistry;

public class JBrokerApplication {

  public static void main(String[] args) {
    ClientConnectionRegistry clientConnectionRegistry = clientConnectionRegistry();
    SubscriptionRegistry subscriptionRegistry = subscriptionRegistry(
        new TopicFilter()
    );
    MessageQueue messageQueue = messageQueue();
    MessageQueueProcessor messageQueueProcessor = new MessageQueueProcessor(
        messageQueue,
        new MessagePublisher(
            subscriptionRegistry,
            clientConnectionRegistry
        )
    );
    FixedHeaderEncoder fixedHeaderEncoder = new FixedHeaderEncoder();
    Broker broker = new Broker(
        new ClientConnectionManager(
            new PacketReader(
                new FixedHeaderReader(),
                new PacketDecoderFactory(
                    new ConnectPacketDecoder(),
                    new PingReqPacketDecoder(),
                    new PublishPacketDecoder(),
                    new SubscribePacketDecoder(),
                    new UnsubscribePacketDecoder(),
                    new DisconnectPacketDecoder()
                )
            ),
            new PacketWriter(
                new PacketEncoderFactory(
                    new ConnackPacketEncoder(fixedHeaderEncoder),
                    new PingRespPacketEncoder(fixedHeaderEncoder),
                    new SubackPacketEncoder(fixedHeaderEncoder),
                    new UnsubackPacketEncoder(fixedHeaderEncoder),
                    new PublishPacketEncoder(fixedHeaderEncoder)
                )
            ),
            new CommandDispatcher(
                new CommandHandlerFactory(
                    new ConnectHandler(),
                    new PingReqHandler(),
                    new PublishHandler(messageQueue),
                    new SubscribeHandler(subscriptionRegistry),
                    new UnsubscribeHandler(subscriptionRegistry),
                    new DisconnectHandler(clientConnectionRegistry)
                )
            ),
            clientConnectionRegistry
        ),
        messageQueueProcessor
    );
    broker.run();
  }

  private static ClientConnectionRegistry clientConnectionRegistry() {
    return new InMemoryClientConnectionRegistry();
  }

  private static SubscriptionRegistry subscriptionRegistry(TopicFilter topicFilter) {
    return new InMemorySubscriptionRegistry(topicFilter);
  }

  private static MessageQueue messageQueue() {
    return new InMemoryMessageQueue();
  }
}
