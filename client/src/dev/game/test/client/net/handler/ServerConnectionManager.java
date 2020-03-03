package dev.game.test.client.net.handler;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.core.net.packet.AbstractConnectionManager;

public class ServerConnectionManager extends AbstractConnectionManager {

    private final IClientGame game;

    public ServerConnectionManager(IClientGame game, Connection connection) {
        super(connection);

        this.game = game;
    }

    @Override
    public void queuePacket(Packet packet) {
        Gdx.app.debug(
                String.format("%s%s received packet\033[0m", "\033[1;33m", "Client"),
                String.format("%s%s\033[0m", "\033[1;33m", packet.getClass().getSimpleName())
        );

        super.queuePacket(packet);
    }
}
