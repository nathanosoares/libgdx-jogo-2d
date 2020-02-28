package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.Gdx;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.PlayerPacketListener;

public class HandshakeListener extends PlayerPacketListener {

    public HandshakeListener(IServerGame game, PlayerConnectionManager playerConnectionManager) {
        super(game, playerConnectionManager);
    }

    @PacketEvent
    public void on(PacketHandshake handshake) {

        if (this.playerConnectionManager.getState() != PacketConnectionState.State.DISCONNECTED) {
            return;
        }

        Gdx.app.log(getClass().getSimpleName(), "Received handshake!");

        // TODO check if is ping

        this.playerConnectionManager.setState(PacketConnectionState.State.HANDSHAKE);
        sendPacket(new PacketConnectionState(this.playerConnectionManager.getState()));

        this.playerConnectionManager.setPacketListener(new LoginListener(this.game, this.playerConnectionManager));
    }
}
