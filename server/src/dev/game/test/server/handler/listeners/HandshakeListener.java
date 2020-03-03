package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.Gdx;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class HandshakeListener extends AbstractPlayerPacketListener {

    public HandshakeListener(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketHandshake handshake) {

        if (this.connectionManager.getState() != PacketConnectionState.State.DISCONNECTED) {
            return;
        }

        Gdx.app.log(getClass().getSimpleName(), "Received handshake!");

        // TODO check if is ping

        this.connectionManager.unregisterListener(HandshakeListener.class);
        this.connectionManager.registerListener(new LoginListener(this.game, this.connectionManager));

        this.connectionManager.setState(PacketConnectionState.State.HANDSHAKE);
    }
}
