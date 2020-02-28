package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketLogin;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketLoginResponse;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.PlayerPacketListener;

import java.util.UUID;

public class LoginListener extends PlayerPacketListener {

    public LoginListener(IServerGame game, PlayerConnectionManager playerConnectionManager) {
        super(game, playerConnectionManager);
    }

    @PacketEvent
    public void on(PacketLogin packet) {
        if (this.playerConnectionManager.getState() != PacketConnectionState.State.HANDSHAKE) {
            return;
        }

        new Thread(() -> {

            this.playerConnectionManager.setUsername(packet.getUsername());

            // TODO fazer as coisas async

            sendPacket(new PacketLoginResponse(UUID.randomUUID()));

            this.playerConnectionManager.setPacketListener(new PreparingListener(this.game, this.playerConnectionManager));

            this.playerConnectionManager.setState(PacketConnectionState.State.PREPARING);
            sendPacket(new PacketConnectionState(this.playerConnectionManager.getState()));
        }).start();
    }
}
