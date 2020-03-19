package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.EntityPositionServerPacket;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.VelocityComponent;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.utils.WorldUtils;

public class VelocitySystem extends IteratingSystem {

    private final Game game;

    private final Vector2 fromPosition = new Vector2();
    private final Vector2 toPosition = new Vector2();
    private final Vector2 velocity = new Vector2();

    public VelocitySystem(Game game) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        fromPosition.set(positionComponent.x, positionComponent.y);
        toPosition.set(fromPosition);

        velocity.set(velocityComponent.x, velocityComponent.y);

        if (velocity.isZero()) {
            return;
        }

        if (!CollisiveComponent.MAPPER.has(entity)) {

            toPosition.x += velocity.x;
            toPosition.y += velocity.y;

        } else {

            Rectangle box = CollisiveComponent.MAPPER.get(entity).box;
            box.setPosition(fromPosition.x, fromPosition.y);

            WorldUtils.adjustCollision(positionComponent.world, box, velocity, toPosition);

        }

        // TODO adicionar evento

        ((IEntity) entity).setPosition(toPosition);

        if (game instanceof IServerGame) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);

            if (fromPosition.x != toPosition.x || fromPosition.y != toPosition.y) {
                IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);

                ((IServerGame) game).getConnectionHandler().broadcastPacket(
                        new EntityPositionServerPacket(identifiable.uuid, toPosition),
                        ((IEntity) entity).getWorld(),
                        connectionComponent != null ? connectionComponent.manager : null
                );
            }
        }
    }
}

