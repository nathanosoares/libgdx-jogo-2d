package dev.game.test.client.net.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IClientGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.EmbeddedServerApplication;
import dev.game.test.core.net.packet.AbstractConnectionManager;

public class ServerConnectionManager extends AbstractConnectionManager {


    private final IClientGame game;

    public ServerConnectionManager(IClientGame game, Connection connection) {
        super(connection);

        this.game = game;
    }

    @Override
    public void sendPacket(Packet packet) {
//        EmbeddedServerApplication serverApplication = ClientApplication.EMBEDDED_SERVER;
//
//        if (packet != null) {
//            this.game
//                    .getConnectionHandler()
//                    .getConnectionManager()
//                    .queuePacket(packet);
//            return;
//        }

        super.sendPacket(packet);
    }
}
