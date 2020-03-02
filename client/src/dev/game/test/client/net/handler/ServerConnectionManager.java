package dev.game.test.client.net.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.EmbeddedServerApplication;
import dev.game.test.core.net.packet.AbstractConnectionManager;

public class ServerConnectionManager extends AbstractConnectionManager {

    public ServerConnectionManager(Connection connection) {
        super(connection);
    }

    @Override
    public void sendPacket(Packet packet) {
        EmbeddedServerApplication serverApplication = ClientApplication.EMBEDDED_SERVER;

        if (packet != null) {
            serverApplication.getGame().getConnectionHandler().getConnectionManager(ClientApplication.DUMMY_CONNECTION)
                    .queuePacket(packet);
            return;
        }

        super.sendPacket(packet);
    }
}
