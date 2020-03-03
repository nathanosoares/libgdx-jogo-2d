package dev.game.test.client.net.handler;

import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.handler.IClientConnectionHandler;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.client.net.handler.listeners.ConnectionStatePacketListener;
import dev.game.test.client.net.handler.listeners.GenericPacketListener;
import dev.game.test.client.net.handler.listeners.WorldPacketListener;
import dev.game.test.core.registry.impl.PacketPayloadSerializerRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientConnectionHandler implements IClientConnectionHandler {

    private final IClientGame game;

    @Getter
    private ServerConnectionManager manager;

    private Client client;

    public void connect(String hostname, int port) throws IOException {
        client = new Client(16384, 2048 * 4);

        PacketPayloadSerializerRegistry registry = this.game.getRegistryManager().getRegistry(Serializer.class);
        registry.apply(client.getKryo());

        client.start();

        client.addListener(new Listener() {
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

                if (o instanceof Packet) {
                    ClientConnectionHandler.this.manager.queuePacket((Packet) o);
                }
            }
        });

        client.connect(5000, hostname, port);
    }

    public void createHandler(Connection connection) {
        this.manager = new ServerConnectionManager(this.game, connection);

        this.manager.registerListener(new ConnectionStatePacketListener(this.game, this.manager));
        this.manager.registerListener(new GenericPacketListener(this.game, this.manager));
        this.manager.registerListener(new WorldPacketListener(this.game, this.manager));

        this.manager.sendPacket(new PacketHandshake(this.game.getUsername()));
    }

}
