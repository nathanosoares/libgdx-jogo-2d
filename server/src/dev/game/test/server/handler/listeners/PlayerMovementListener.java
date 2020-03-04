package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketPlayerMovement;
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.net.packet.server.PacketEntityPosition;
import dev.game.test.api.net.packet.server.PacketPlayerMovementResponse;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.systems.MovementSystem;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class PlayerMovementListener extends AbstractPlayerPacketListener {

    public PlayerMovementListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }

    @Subscribe
    public void on(PacketPlayerMovement packet) {

        if (Math.abs(packet.getDeltaX()) > 1 / 40f || Math.abs(packet.getDeltaY()) > 1 / 40f) {

            System.out.println(Math.abs(packet.getDeltaX()));
            System.out.println(Math.abs(packet.getDeltaY()));

            this.manager.sendPacket(new PacketPlayerMovementResponse(
                    packet.getSequenceNumber(), this.manager.getPlayer().getPosition()
            ));

            return;
        }

        MovementComponent movementComponent = MovementComponent.MAPPER.get(this.manager.getPlayer());

        movementComponent.deltaX = packet.getDeltaX();
        movementComponent.deltaY = packet.getDeltaY();

        IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(this.manager.getPlayer());

        this.game.getConnectionHandler().broadcastPacket(new PacketEntityMovement(
                identifiable.uuid, movementComponent.deltaX, movementComponent.deltaY
        ), this.manager);

        Vector2 toPosition = MovementSystem.processEntity((Game) this.game, this.manager.getPlayer());

        this.game.getConnectionHandler().broadcastPacket(new PacketEntityPosition(
                identifiable.uuid, toPosition, null
        ), this.manager);

        this.manager.sendPacket(new PacketPlayerMovementResponse(
                packet.getSequenceNumber(), toPosition
        ));
    }
}
