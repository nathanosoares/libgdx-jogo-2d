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
        if (this.manager.getState() != PacketConnectionState.State.HANDSHAKE) {
            return;
        }

        new Thread(() -> {

            this.manager.setUsername(packet.getUsername());
            this.manager.setPlayerUUID(UUID.randomUUID());

            // TODO fazer as coisas async

            this.manager.sendPacket(new PacketLoginResponse(this.manager.getPlayerUUID()));

            this.manager.unregisterListener(LoginListener.class);
            this.manager.registerListener(new PreparingListener(this.game, this.manager));

            this.manager.setState(PacketConnectionState.State.PREPARING_INFO);
        }).start();
    }
}
