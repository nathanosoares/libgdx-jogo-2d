package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketLogin;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketLoginResponse;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class LoginListener extends AbstractPlayerPacketListener {

    public LoginListener(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketLogin packet) {
        if (this.connectionManager.getState() != PacketConnectionState.State.HANDSHAKE) {
            return;
        }

        new Thread(() -> {

            this.connectionManager.setUsername(packet.getUsername());
            this.connectionManager.setPlayerUUID(UUID.randomUUID());

            // TODO fazer as coisas async

            this.connectionManager.sendPacket(new PacketLoginResponse(this.connectionManager.getPlayerUUID()));

            this.connectionManager.unregisterListener(LoginListener.class);
            this.connectionManager.registerListener(new PreparingListener(this.game, this.connectionManager));

            this.connectionManager.setState(PacketConnectionState.State.PREPARING);
        }).start();
    }
}
