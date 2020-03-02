package dev.game.test.api.net;

import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;

public interface IConnectionManager {

    PacketConnectionState.State getState();

    void sendPacket(Packet packet);

    void queuePacket(Packet packet);

    void processQueue();

    void registerListener(IPacketListener listener);

    void unregisterListener(Class<? extends IPacketListener> clazz);
}
