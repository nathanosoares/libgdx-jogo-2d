package dev.game.test.client.net.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.client.GameApplication;
import dev.game.test.client.net.ConnectionHandler;
import dev.game.test.client.net.packet.EnumPacket;
import dev.game.test.client.net.packet.Packet;
import dev.game.test.client.net.packet.client.PacketWorldRequest;
import dev.game.test.client.net.packet.handshake.PacketHandshake;
import dev.game.test.client.net.packet.server.PacketEntitySpawn;
import dev.game.test.client.net.packet.server.PacketPlayerInfo;
import dev.game.test.client.net.packet.server.PacketWorldLayerData;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientConnectionHandler implements ConnectionHandler {

    private final GameApplication application;

    //

    public ClientPacketHandler serverConnection;

    private Client client;

    //

    private ClientConnectionState state = ClientConnectionState.HANDSHAKE;

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

            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

                serverConnection = null;
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                if (serverConnection != null && o instanceof Packet) {
                    serverConnection.callPacket((Packet) o);
                }
            }
        });

        this.client.connect(5000, hostname, port);
    }

    /*

     */

    public void onHandshake(PacketHandshake packet) {
        this.state = ClientConnectionState.PREPARING;

        serverConnection.sendPacket(new PacketWorldRequest(30));
    }

    public void onPlayerInfo(PacketPlayerInfo packet) {
    }

    public void onWorldLayerData(PacketWorldLayerData packet) {

    }

    public void onEntitySpawn(PacketEntitySpawn packet) {
//        WorldClient worldClient = application.getWorldClient();
//
//        Player player = worldClient.getPlayer(packet.getId());
//
//        if (player == null) {
//            player = new Player(packet.getId());
//            worldClient.addEntity(player, packet.getPosition());
//        }
    }

}
