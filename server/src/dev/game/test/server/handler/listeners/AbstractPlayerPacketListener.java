package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.net.packet.AbstractPacketListener;
import dev.game.test.server.handler.PlayerConnectionManager;

public abstract class AbstractPlayerPacketListener extends AbstractPacketListener<IServerGame, PlayerConnectionManager> {

    public AbstractPlayerPacketListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }

    public void sendPacket(Packet packet) {
        this.manager.sendPacket(packet);
    }

    public void broadcastPacket(Packet packet) {
        this.game.getConnectionHandler().broadcastPacket(packet);
    }

    public void broadcastPacket(Packet packet, IConnectionManager exclude) {
        this.game.getConnectionHandler().broadcastPacket(packet, exclude);
    }

    public void broadcastPacket(Packet packet, IWorld world) {
        this.game.getConnectionHandler().broadcastPacket(packet, world);
    }

    public void broadcastPacket(Packet packet, IWorld world, IConnectionManager exclude) {
        this.game.getConnectionHandler().broadcastPacket(packet, world, exclude);
    }
}
