package dev.game.test.server.handler;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Maps;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.server.handler.IServerConnectionHandler;
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
        for (PlayerConnectionManager playerConnectionManager : connections.values()) {
            playerConnectionManager.processQueue();
        }
    }

    public PlayerConnectionManager getConnectionManager(Connection connection) {
        return this.connections.get(connection);
    }

    public void createHandler(Connection connection) {
        PlayerConnectionManager playerConnectionManager = new PlayerConnectionManager(this.game, connection);

        playerConnectionManager.registerListener(new HandshakeListener(this.game, playerConnectionManager));

        ServerConnectionHandler.this.connections.put(connection, playerConnectionManager);
    }

    private class ServerListener extends Listener {

        @Override
        public void connected(Connection connection) {
            Gdx.app.debug("ServerConnectionHandler", "Connected");
            super.connected(connection);

            createHandler(connection);
        }

        @Override
        public void disconnected(Connection connection) {
            Gdx.app.debug("ServerConnectionHandler", "Disconnected");
            super.disconnected(connection);

            ServerConnectionHandler.this.connections.remove(connection);
        }

        @Override
        public void received(Connection connection, Object object) {
            Gdx.app.debug("ServerConnectionHandler", "Received");
            super.received(connection, object);

            PlayerConnectionManager playerConnectionManager = ServerConnectionHandler.this.connections.get(connection);

            if (playerConnectionManager != null && object instanceof Packet) {
                playerConnectionManager.queuePacket((Packet) object);
            }
        }
    }
}
