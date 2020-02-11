package dev.game.test.net.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dev.game.test.net.GameClientConnection;
import dev.game.test.net.GameNet;
import dev.game.test.net.GameServerConnection;
import dev.game.test.net.handshake.PacketHandshake;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;
import dev.game.test.net.server.ServerGameNet;
import java.io.IOException;

public class ClientGameNet implements GameNet {

    public GameServerConnection serverConnection;

    private Client client;

    public void connect(String hostname, int port) throws IOException {
        this.client = new Client();

        Kryo kyro = this.client.getKryo();
        EnumPacket.registry.forEach((id, packet) -> kyro.register(packet, id));

        this.client.start();

        this.client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                GameServerConnection _serverConnection = new GameServerConnection(ClientGameNet.this, connection);
                serverConnection = _serverConnection;

                System.out.println("Sending packet to server");
                serverConnection.sendPacket(new PacketHandshake("Hello World!"));
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

                serverConnection = null;
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                if(serverConnection != null && o instanceof Packet) {
                    serverConnection.callPacket((Packet) o);
                }
            }
        });

        this.client.connect(5000, hostname, port);
    }

}
