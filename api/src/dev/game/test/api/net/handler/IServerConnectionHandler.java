package dev.game.test.api.net.handler;

import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.world.IWorld;

import java.io.IOException;

public interface IServerConnectionHandler {

    void start(int port) throws IOException;

    void processQueues();

    void broadcastPacket(Packet packet);

    void broadcastPacket(Packet packet, IConnectionManager exclude);

    void broadcastPacket(Packet packet, IWorld world);

    void broadcastPacket(Packet packet, IWorld world, IConnectionManager exclude);
}
