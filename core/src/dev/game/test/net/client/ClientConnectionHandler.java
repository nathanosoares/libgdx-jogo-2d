package dev.game.test.net.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.net.GameNet;
import dev.game.test.net.handshake.PacketHandshake;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;

import java.io.IOException;

public class ClientConnectionHandler implements GameNet {

    public ClientPacketHandler serverConnection;

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
                ClientPacketHandler _serverConnection = new ClientPacketHandler(ClientConnectionHandler.this, connection);
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
