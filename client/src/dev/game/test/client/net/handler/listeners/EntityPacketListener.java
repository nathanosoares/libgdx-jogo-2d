package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.net.packet.server.PacketEntityPosition;
import dev.game.test.api.net.packet.server.PacketEntitySpawn;
import dev.game.test.api.net.packet.server.PacketEntityState;
import dev.game.test.client.GameUtils;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.core.entity.components.StateComponent;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class EntityPacketListener extends AbstractServerPacketListener {

    public EntityPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketEntityState packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null && StateComponent.MAPPER.has((Entity) entity)) {
            StateComponent stateComponent = StateComponent.MAPPER.get((Entity) entity);

            stateComponent.machine.changeState(packet.getState());
        }
    }

    @Subscribe
    public void on(PacketEntityPosition packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            entity.setPosition(packet.getPosition());
        }
    }

    @Subscribe
    public void on(PacketEntityMovement packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            MovementComponent movementComponent = MovementComponent.MAPPER.get((Entity) entity);

            movementComponent.deltaX = packet.getDeltaX();
            movementComponent.deltaY = packet.getDeltaY();
        }
    }

    @Subscribe
    public void on(PacketEntitySpawn packet) {
        if (packet.getEntityId().equals(this.game.getClientManager().getPlayer().getId())) {
            return;
        }

        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            this.game.getEngine().removeEntity((Entity) entity);
            this.game.getClientManager().removeEntity(entity);
        }

        Player player = GameUtils.buildClientPlayer(packet.getEntityId(), UUID.randomUUID().toString());

        player.setPosition(packet.getPosition());
        player.setWorld(this.game.getClientManager().getPlayer().getWorld());

        this.game.getEngine().addEntity(player);
        this.game.getClientManager().addEntity(player);
    }
}
