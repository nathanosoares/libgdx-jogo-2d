package dev.game.test.net.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Maps;
import dev.game.test.net.ConnectionHandler;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;

import java.io.IOException;
import java.util.Map;

public class ServerConnectionHandler implements ConnectionHandler {

    private Map<Connection, ServerPacketHandler> clientConnections = Maps.newHashMap();

    /*

     */

    private Server server;

    public void start(int port) throws IOException {
        this.server = new Server();

        Kryo kyro = this.server.getKryo();
        EnumPacket.registry.forEach((id, packet) -> kyro.register(packet, id));

        this.server.start();
        this.server.bind(port);

        this.server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);

                ServerPacketHandler clientConnection = new ServerPacketHandler(ServerConnectionHandler.this, connection);
                clientConnections.put(connection, clientConnection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

                clientConnections.remove(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                ServerPacketHandler clientConnection = clientConnections.get(connection);
                if(clientConnection != null && o instanceof Packet) {
                    clientConnection.callPacket((Packet) o);
                }
            }
        });
    }

}
