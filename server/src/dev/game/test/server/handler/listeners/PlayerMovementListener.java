package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.DirectionClientPacket;
import dev.game.test.api.net.packet.client.HitClientPacket;
import dev.game.test.api.net.packet.client.MovementClientPacket;
import dev.game.test.api.net.packet.server.EntityDirectionServerPacket;
import dev.game.test.api.net.packet.server.PlayerMovementResponseServerPacket;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.player.componenets.HitComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class PlayerMovementListener extends AbstractPlayerPacketListener {

    public PlayerMovementListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }

    @Subscribe
    public void on(MovementClientPacket packet) {

        MovementComponent movementComponent = MovementComponent.MAPPER.get(this.manager.getPlayer());

        if (Math.abs(packet.getDeltaX()) > 1 / 40f || Math.abs(packet.getDeltaY()) > 1 / 40f) {
            this.manager.sendPacket(new PlayerMovementResponseServerPacket(
                    packet.getSequenceNumber(), this.manager.getPlayer().getPosition()
            ));

            return;
        }

        movementComponent.sequenceId = packet.getSequenceNumber();
        movementComponent.updatedAt = System.currentTimeMillis();
        movementComponent.deltaX += packet.getDeltaX();
        movementComponent.deltaY += packet.getDeltaY();
    }

    @Subscribe
    public void on(HitClientPacket packet) {
        HitComponent hitComponent = HitComponent.MAPPER.get(this.manager.getPlayer());
        hitComponent.pending = true;
    }

    @Subscribe
    public void on(DirectionClientPacket packet) {
        DirectionComponent directionComponent = DirectionComponent.MAPPER.get(this.manager.getPlayer());
        directionComponent.degrees = packet.getDegrees();

        this.game.getConnectionHandler().broadcastPacket(new EntityDirectionServerPacket(
                this.manager.getPlayerUUID(),
                packet.getDegrees()
        ), this.manager);
    }
}
