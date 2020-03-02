package dev.game.test.client.net.handler;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.game.test.api.IClientGame;
import dev.game.test.api.client.handler.IClientConnectionHandler;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.client.net.handler.listeners.ConnectionStatePacketListener;
import dev.game.test.client.net.handler.listeners.GenericPacketListener;
import dev.game.test.client.net.handler.listeners.WorldPacketListener;
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
        client = new Client();

        Kryo kyro = client.getKryo();

        EnumPacket.registry.forEach((id, packet) -> kyro.register(packet));

        client.start();

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Gdx.app.debug("ClientConnectionHandler", "Connected");
                super.connected(connection);

                createHandler(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                Gdx.app.debug("ClientConnectionHandler", "Disconnected");
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                Gdx.app.debug("ClientConnectionHandler", "Received");
                super.received(connection, o);

                if (o instanceof Packet) {
                    ClientConnectionHandler.this.connectionManager.queuePacket((Packet) o);
                }
            }
        });

        client.connect(5000, hostname, port);
    }

    public void queuePacket(Packet packet) {
        this.connectionManager.queuePacket(packet);
    }

    @Override
    public void processQueue() {
        this.connectionManager.processQueue();
    }

    public void createHandler(Connection connection) {
        this.connectionManager = new ServerConnectionManager(this.game, connection);

        this.connectionManager.registerListener(new ConnectionStatePacketListener(this.game, this.connectionManager));
        this.connectionManager.registerListener(new GenericPacketListener(this.game, this.connectionManager));
        this.connectionManager.registerListener(new WorldPacketListener(this.game, this.connectionManager));

        this.connectionManager.sendPacket(new PacketHandshake(this.game.getUsername()));
    }

}
