package dev.game.test.server.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Maps;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.server.handler.IServerConnectionHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class ServerConnectionHandler implements IServerConnectionHandler {

    private final IServerGame serverGame;

    //

    @Getter
    private Map<Connection, PlayerPacketHandler> connections = Maps.newHashMap();

    private Server server;

    @Override
    public void start(int port) throws IOException {
        this.server = new Server();

        this.server.start();
        this.server.bind(port);
        this.server.addListener(new ServerListener());
    }

    @Override
    public void processQueue() {
        for(PlayerPacketHandler packetHandler : connections.values()) {
            packetHandler.processQueue();
        }
    }

    /*

     */

    public PlayerPacketHandler getConnection(Connection connection) {
        return this.connections.get(connection);
    }

    public void createHandler(Connection connection) {
        ServerConnectionHandler.this.connections.put(connection, new PlayerPacketHandler(serverGame, connection));
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

            ServerConnectionHandler.this.connections.remove(connection);
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);

            PlayerPacketHandler clientConnection = ServerConnectionHandler.this.connections.get(connection);

            if (clientConnection != null && object instanceof Packet) {
                clientConnection.queuePacket((Packet) object);
            }
        }
    }
}
