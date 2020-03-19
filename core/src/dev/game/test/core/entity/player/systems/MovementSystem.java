package dev.game.test.core.entity.player.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.EntityMovementServerPacket;
import dev.game.test.api.net.packet.server.PlayerMovementResponseServerPacket;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.core.entity.player.componenets.WalkSpeedComponent;
import dev.game.test.core.utils.WorldUtils;

public class MovementSystem extends IteratingSystem {

    private final Game game;

    private final Vector2 movement = new Vector2();
    private final Vector2 from = new Vector2();
    private final Vector2 position = new Vector2();

    public MovementSystem(Game game) {
        super(Family.all(MovementComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);
        PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);
        WalkSpeedComponent walkSpeedComponent = WalkSpeedComponent.MAPPER.get(entity);

        position.set(positionComponent.x, positionComponent.y);
        from.set(positionComponent.x, positionComponent.y);
        movement.set(movementComponent.deltaX * walkSpeedComponent.speed, movementComponent.deltaY * walkSpeedComponent.speed);

        Rectangle box = CollisiveComponent.MAPPER.get(entity).box;
        box.setPosition(position);

        WorldUtils.adjustCollision(positionComponent.world, box, movement, position);

        positionComponent.x = position.x;
        positionComponent.y = position.y;

        if (game instanceof IServerGame && ConnectionComponent.MAPPER.has(entity)) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);

            if (movementComponent.sequenceId != -1) {
                connectionComponent.manager.sendPacket(new PlayerMovementResponseServerPacket(movementComponent.sequenceId, position));

                movementComponent.sequenceId = -1;
            }

            IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);

            ((IServerGame) game).getConnectionHandler().broadcastPacket(
                    new EntityMovementServerPacket(
                            identifiable.uuid,
                            movementComponent.deltaX,
                            movementComponent.deltaY,
                            from.x,
                            from.y
                    ),
                    ((IEntity) entity).getWorld(),
                    connectionComponent.manager
            );
        }

        movementComponent.deltaX = 0;
        movementComponent.deltaY = 0;
    }
}
