package dev.game.test.client.net.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.api.IClientGame;
import dev.game.test.api.client.handler.IClientConnectionHandler;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.core.net.packet.EnumPacket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientConnectionHandler implements IClientConnectionHandler {

    private final IClientGame game;

    @Getter
    private ServerConnectionManager connectionManager;

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

                createHandler(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);

                if (ClientConnectionHandler.this.connectionManager.getPacketListener() != null && o instanceof Packet) {
                    ClientConnectionHandler.this.connectionManager.getPacketListener().queuePacket((Packet) o);
                }
            }
        });
        this.client.connect(5000, hostname, port);
    }

    public void queuePacket(Packet packet) {
        this.connectionManager.getPacketListener().queuePacket(packet);
    }

    @Override
    public void processQueue() {
        this.connectionManager.getPacketListener().processQueue();
    }

    public void createHandler(Connection connection) {
        this.connectionManager = new ServerConnectionManager(connection);
        this.connectionManager.setPacketListener(new ServerPacketListener(this.game, this.connectionManager));
        this.connectionManager.getPacketListener().sendPacket(new PacketHandshake(this.game.getUsername()));
    }

}
