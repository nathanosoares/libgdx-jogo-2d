package dev.game.test.server.handler;

import com.badlogic.ashley.core.Entity;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Maps;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.handler.IServerConnectionHandler;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.registry.impl.PacketPayloadSerializerRegistry;
import dev.game.test.server.handler.listeners.HandshakeListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class ServerConnectionHandler implements IServerConnectionHandler {

    private final IServerGame game;

    @Getter
    private Map<Connection, PlayerConnectionManager> connections = Maps.newHashMap();

    private Server server;

    @Override
    public void start(int port) throws IOException {
        this.server = new Server(16384, 2048 * 4);

        PacketPayloadSerializerRegistry registry = this.game.getRegistryManager().getRegistry(Serializer.class);
        registry.apply(this.server.getKryo());

        this.server.start();
        this.server.addListener(new ServerListener());
        this.server.bind(port);
    }

    @Override
    public void processQueues() {
        for (PlayerConnectionManager manager : connections.values()) {
            manager.processQueue();
        }
    }


    @Override
    public void broadcastPacket(Packet packet, IWorld world) {
        broadcastPacket(packet, world, null);
    }


    @Override
    public void broadcastPacket(Packet packet, IWorld world, IConnectionManager exclude) {
        for (IPlayer player : world.getPlayers()) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get((Entity) player);

            if (connectionComponent.manager != exclude) {
                connectionComponent.manager.sendPacket(packet);
            }
        }
    }

    @Override
    public void broadcastPacket(Packet packet) {
        broadcastPacket(packet, (IConnectionManager) null);
    }

    @Override
    public void broadcastPacket(Packet packet, IConnectionManager exclude) {
        for (PlayerConnectionManager connectionManager : connections.values()) {
            if (exclude != connectionManager) {
                connectionManager.sendPacket(packet);
            }
        }
    }

    public void createHandler(Connection connection) {
        PlayerConnectionManager playerConnectionManager = new PlayerConnectionManager(this.game, connection);

        playerConnectionManager.registerListener(new HandshakeListener(this.game, playerConnectionManager));

        ServerConnectionHandler.this.connections.put(connection, playerConnectionManager);
    }

    private class ServerListener extends Listener {

        @Override
        public void connected(Connection connection) {
            super.connected(connection);

            createHandler(connection);
        }

        @Override
        public void disconnected(Connection connection) {
            super.disconnected(connection);

            PlayerConnectionManager manager = ServerConnectionHandler.this.connections.remove(connection);

            if (manager != null) {

                if (manager.getPlayer() != null) {
                    Player player = manager.getPlayer();

                    if (player.getWorld() != null) {
                        player.getWorld().destroyEntity(player);
                    }

                    ServerConnectionHandler.this.game.getEngine().removeEntity(player);
                }
            }
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);

            PlayerConnectionManager playerConnectionManager = ServerConnectionHandler.this.connections.get(connection);

            if (playerConnectionManager != null && object instanceof Packet) {
                playerConnectionManager.queuePacket((Packet) object);
            }
        }
    }
}
