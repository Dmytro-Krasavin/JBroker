package com.jbroker.packet.model.bidirectional;

import com.jbroker.packet.model.inbound.ClientToServerPacket;
import com.jbroker.packet.model.outbound.ServerToClientPacket;

public interface BidirectionalPacket extends ClientToServerPacket, ServerToClientPacket {

}
