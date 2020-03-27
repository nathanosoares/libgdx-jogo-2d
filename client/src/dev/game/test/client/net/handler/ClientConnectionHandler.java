package dev.game.test.client.net.handler;

import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
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

    @Getter
    private Client client;

    public void connect(String hostname, int port) throws IOException {
        client = new Client(16384, 2048 * 4);

        PacketPayloadSerializerRegistry registry = this.game.getRegistryManager().getRegistry(Serializer.class);
        registry.apply(client.getKryo());

        client.start();

        client.addListener(new ClientListener());

        client.connect(20000, hostname, port);
    }

    public void createHandler(Connection connection) {
        this.manager = new ServerConnectionManager(this.game, connection);

        this.manager.registerListener(new ConnectionStatePacketListener(this.game, this.manager));
        this.manager.registerListener(new GenericPacketListener(this.game, this.manager));
        this.manager.registerListener(new WorldPacketListener(this.game, this.manager));

        this.manager.sendPacket(new PacketHandshake(this.game.getUsername()));
    }

    private class ClientListener extends Listener {
        @Override
        public void connected(Connection connection) {
            super.connected(connection);

            createHandler(connection);


            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    client.updateReturnTripTime();
                }
            }, 0, 3);
        }

        @Override
        public void disconnected(Connection connection) {
            super.disconnected(connection);
        }

        @Override
        public void received(Connection connection, Object object) {
            super.received(connection, object);

            if (object instanceof Packet) {
                ClientConnectionHandler.this.manager.queuePacket((Packet) object);
            }

            if (object instanceof FrameworkMessage.Ping) {
                FrameworkMessage.Ping ping = (FrameworkMessage.Ping) object;

                if (ping.isReply) {
                    System.out.println("Ping: " + connection.getReturnTripTime());
                }
            }
        }
    }
}
