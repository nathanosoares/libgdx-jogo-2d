package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.client.GameUtils;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.StateComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class EntityPacketListener extends AbstractServerPacketListener {

    public EntityPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(EntityStateServerPacket packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null && StateComponent.MAPPER.has((Entity) entity)) {
            StateComponent stateComponent = StateComponent.MAPPER.get((Entity) entity);

            stateComponent.machine.changeState(packet.getState());
        }
    }

    @Subscribe
    public void on(EntityPositionServerPacket packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            entity.setPosition(packet.getPosition());
        }
    }

    @Subscribe
    public void on(EntityMovementServerPacket packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null && MovementComponent.MAPPER.has((Entity) entity)) {
            MovementComponent movementComponent = MovementComponent.MAPPER.get((Entity) entity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get((Entity) entity);

            movementComponent.deltaX = packet.getDeltaX();
            movementComponent.deltaY = packet.getDeltaY();

//            positionComponent.x = packet.getFromX();
//            positionComponent.y = packet.getFromY();

        }
    }

    @Subscribe
    public void on(EntityDirectionServerPacket packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null && DirectionComponent.MAPPER.has((Entity) entity)) {
            DirectionComponent directionComponent = DirectionComponent.MAPPER.get((Entity) entity);

            directionComponent.degrees = packet.getDegrees();
        }
    }

    @Subscribe
    public void on(EntityDestroyServerPacket packet) {
        if (packet.getEntityId().equals(this.game.getClientManager().getPlayer().getId())) {
            return;
        }

        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            this.game.getClientManager().removeEntity(entity);
        }
    }

    @Subscribe
    public void on(EntitySpawnServerPacket packet) {
        if (packet.getEntityId().equals(this.game.getClientManager().getPlayer().getId())) {
            return;
        }

        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            this.game.getClientManager().removeEntity(entity);
        }

        if (packet.getEntityType() == EnumEntityType.PLAYER) {
            entity = GameUtils.buildClientPlayer(packet.getEntityId(), UUID.randomUUID().toString());
            entity.setPosition(this.game.getClientManager().getPlayer().getWorld(), packet.getPosition());
        } else {
            entity = this.game.getClientManager().getPlayer().getWorld().createEntity(packet.getEntityId(), packet.getEntityType());
        }

        entity.setDirection(packet.getDirection());

        this.game.getClientManager().getPlayer().getWorld().spawnEntity(entity, packet.getPosition());
    }
}
