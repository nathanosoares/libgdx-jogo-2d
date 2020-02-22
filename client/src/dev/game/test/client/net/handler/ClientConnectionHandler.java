package dev.game.test.client.net.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.core.net.packet.EnumPacket;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientConnectionHandler {

    //

    private final IClientGame clientGame;

    private final IPlayer player;

    //

    private ClientPacketHandler packetHandler;

    private Client client;

    //

    public void connect(String hostname, int port) throws IOException {
        this.client = new Client();

        Kryo kyro = this.client.getKryo();
        EnumPacket.registry.forEach((id, packet) -> kyro.register(packet, id));

        this.client.start();
        this.client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);

                createHandler(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

                packetHandler = null;
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                if (packetHandler != null && o instanceof Packet) {
                    packetHandler.queuePacket((Packet) o);
                }
            }
        });
        this.client.connect(5000, hostname, port);
    }

    public void createHandler(Connection connection) {
        ClientPacketHandler _packetHandler = new ClientPacketHandler(clientGame, connection);
        packetHandler = _packetHandler;

        packetHandler.sendPacket(new PacketHandshake(player.getId()));
    }

    /*

     */

}