package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketPlayerMovement;
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.net.packet.server.PacketPlayerMovementResponse;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class PlayerMovementListener extends AbstractPlayerPacketListener {

    public PlayerMovementListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }

    @Subscribe
    public void on(PacketPlayerMovement packet) {

        MovementComponent movementComponent = MovementComponent.MAPPER.get(this.manager.getPlayer());

        if (Math.abs(packet.getDeltaX()) > 1 / 40f || Math.abs(packet.getDeltaY()) > 1 / 40f) {

            this.manager.sendPacket(new PacketPlayerMovementResponse(
                    packet.getSequenceNumber(), this.manager.getPlayer().getPosition()
            ));

            return;
        }

        DirectionComponent directionComponent = DirectionComponent.MAPPER.get(this.manager.getPlayer());

        movementComponent.sequenceId = packet.getSequenceNumber();
        movementComponent.updatedAt = System.currentTimeMillis();
        movementComponent.deltaX += packet.getDeltaX();
        movementComponent.deltaY += packet.getDeltaY();
        directionComponent.degrees = packet.getDegrees();

        IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(this.manager.getPlayer());

        this.game.getConnectionHandler().broadcastPacket(
                new PacketEntityMovement(identifiable.uuid, packet.getDeltaX(), packet.getDeltaY(), packet.getDegrees()),
                this.manager.getPlayer().getWorld(),
                this.manager
        );
    }
}
