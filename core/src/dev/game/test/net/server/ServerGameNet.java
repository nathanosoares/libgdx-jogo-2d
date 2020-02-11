package dev.game.test.net.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.game.test.net.GameClientConnection;
import dev.game.test.net.GameNet;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

public class ServerGameNet implements GameNet {

    private Map<Connection, GameClientConnection> clientConnections = Maps.newHashMap();

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

                System.out.println("SV - Connected");
                GameClientConnection clientConnection = new GameClientConnection(ServerGameNet.this, connection);
                clientConnections.put(connection, clientConnection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                System.out.println("SV - Disconnected");
                clientConnections.remove(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                System.out.println("SV - Received");
                GameClientConnection clientConnection = clientConnections.get(connection);
                if(clientConnection != null && o instanceof Packet) {
                    clientConnection.callPacket((Packet) o);
                }
            }
        });
    }

}
