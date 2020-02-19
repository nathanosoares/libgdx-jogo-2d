package dev.game.test.server.handler;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Maps;
import dev.game.test.core.packet.Packet;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;

public class ServerConnectionHandler {

    @Getter
    private Map<Connection, ConnectionPacketHandler> connections = Maps.newHashMap();

    private Server server;

    public void start(int port) throws IOException {
        this.server = new Server();

        this.server.start();
        this.server.bind(port);
        this.server.addListener(new ServerListener());
    }

    private class ServerListener extends Listener {

        @Override
        public void connected(Connection connection) {
            super.connected(connection);

            ServerConnectionHandler.this.connections.put(connection, new ConnectionPacketHandler(connection));
        }

        @Override
        public void disconnected(Connection connection) {
            super.disconnected(connection);

            ServerConnectionHandler.this.connections.remove(connection);
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);

            ConnectionPacketHandler clientConnection = ServerConnectionHandler.this.connections.get(connection);

            if (clientConnection != null && object instanceof Packet) {

            }
        }
    }
}
