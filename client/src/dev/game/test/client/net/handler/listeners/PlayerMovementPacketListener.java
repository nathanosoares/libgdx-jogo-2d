package dev.game.test.client.net.handler.listeners;

import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.server.PacketPlayerMovementResponse;
import dev.game.test.client.entity.systems.PlayerMovementControllerSystem;
import dev.game.test.client.net.handler.ServerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class PlayerMovementPacketListener extends AbstractServerPacketListener {

    public PlayerMovementPacketListener(IClientGame game, ServerConnectionManager manager) {
        super(game, manager);
    }

    @Subscribe
    public void on(PacketPlayerMovementResponse packet) {

        if (packet.getSequenceNumber() == PlayerMovementControllerSystem.sequenceNumber - 1) {
            System.out.println("PacketEntityMovementResponse");
            this.game.getClientManager().getPlayer().setPosition(packet.getPosition());
        }

    }
}
