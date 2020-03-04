package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketPlayerMovement;
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.net.packet.server.PacketPlayerMovementResponse;
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

        if (Math.abs(packet.getDeltaX()) > 1 / 40f || Math.abs(packet.getDeltaY()) > 1 / 40f) {

            this.manager.sendPacket(new PacketPlayerMovementResponse(
                    packet.getSequenceNumber(), this.manager.getPlayer().getPosition()
            ));

            return;
        }

        MovementComponent movementComponent = MovementComponent.MAPPER.get(this.manager.getPlayer());

        movementComponent.sequenceId = packet.getSequenceNumber();
        float deltaX = movementComponent.deltaX = packet.getDeltaX();
        float deltaY = movementComponent.deltaY = packet.getDeltaY();

//        State state = PlayerStateSystem.processEntity((Game) this.game, this.manager.getPlayer());

        IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(this.manager.getPlayer());

//        this.game.getConnectionHandler().broadcastPacket(new PacketEntityState(identifiable.uuid, state), this.manager);
        this.game.getConnectionHandler().broadcastPacket(new PacketEntityMovement(identifiable.uuid, deltaX, deltaY), this.manager);

//        Vector2 toPosition = MovementSystem.processEntity((Game) this.game, this.manager.getPlayer());
//
//        this.game.getConnectionHandler().broadcastPacket(new PacketEntityPosition(identifiable.uuid, toPosition), this.manager);
//
//        this.manager.sendPacket(new PacketPlayerMovementResponse(
//                packet.getSequenceNumber(), toPosition
//        ));
    }
}
