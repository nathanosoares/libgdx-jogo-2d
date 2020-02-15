package dev.game.test.net.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.GameApplication;
import dev.game.test.net.ConnectionHandler;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;
import dev.game.test.net.packet.client.PacketWorldRequest;
import dev.game.test.net.packet.handshake.PacketHandshake;
import dev.game.test.net.packet.server.PacketEntitySpawn;
import dev.game.test.net.packet.server.PacketPlayerInfo;
import dev.game.test.net.packet.server.PacketWorldLayerData;
import dev.game.test.world.World;
import dev.game.test.world.entity.Player;
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

                serverConnection.sendPacket(new PacketHandshake(application.getPlayer().getId()));
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
        World world = application.getWorld();
        Player player = world.getPlayer(packet.getId());

        if (player == null) {
            player = new Player(packet.getId());
            world.addEntity(player, packet.getPosition());
        }
    }

}
