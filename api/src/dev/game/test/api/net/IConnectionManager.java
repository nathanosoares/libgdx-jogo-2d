package dev.game.test.api.net;

import dev.game.test.api.net.packet.handshake.PacketConnectionState;

public interface IConnectionManager {

    PacketConnectionState.State getState();
}
