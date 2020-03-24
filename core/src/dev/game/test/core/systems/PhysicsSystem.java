package dev.game.test.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.game.test.api.IGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.block.IPhysicBlockState;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.EntityMovementServerPacket;
import dev.game.test.api.net.packet.server.EntityPositionServerPacket;
import dev.game.test.api.net.packet.server.PlayerMovementResponseServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.block.impl.BlockWater;
import dev.game.test.core.entity.components.BodyComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.VelocityComponent;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;


public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1 / 45f;
    private static float accumulator = 0f;

    private IGame game;

    private Array<Entity> bodiesQueue;

    public PhysicsSystem(IGame game) {
        super(Family.all().get());

        this.game = game;

        this.bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        for (Entity entity : bodiesQueue) {
            BodyComponent bodyComponent = BodyComponent.MAPPER.get(entity);

            if (MovementComponent.MAPPER.has(entity)) {
                processMovement(entity, bodyComponent);
            }

            if (VelocityComponent.MAPPER.has(entity)) {
                processVelocity(entity, bodyComponent);
            }
        }

        for (IWorld world : this.game.getGameManager().getWorlds()) {
            world.getBox2dWorld().step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;
        }

        for (Entity entity : bodiesQueue) {
            if (PositionComponent.MAPPER.has(entity)) {
                BodyComponent bodyComponent = BodyComponent.MAPPER.get(entity);
                processPosition(entity, bodyComponent);
            }
        }

        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (BodyComponent.MAPPER.has(entity)) {
            bodiesQueue.add(entity);
        }
    }

    private final Vector2 movement = new Vector2();

    private void processMovement(Entity entity, BodyComponent bodyComponent) {
        MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);

        float speed = movementComponent.speed * 50;

        IEntity iEntity = (IEntity) entity;

        Vector2 entityPosition = iEntity.getPosition();

        IBlockState blockState = iEntity.getWorld().getLayers()[0].getBlockState(entityPosition.x, entityPosition.y);

        if (blockState != null && blockState.getBlock() instanceof BlockWater) {
            speed *= 0.3f;
        }

        movement.set(movementComponent.deltaX * speed, movementComponent.deltaY * speed);

        bodyComponent.body.setLinearVelocity(movement.x, movement.y);

        movementComponent.deltaX = 0;
        movementComponent.deltaY = 0;

        if (game instanceof IServerGame && ConnectionComponent.MAPPER.has(entity)) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);

            IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);

            ((IServerGame) game).getConnectionHandler().broadcastPacket(
                    new EntityMovementServerPacket(
                            identifiable.uuid,
                            movementComponent.deltaX,
                            movementComponent.deltaY,
                            positionComponent.x,
                            positionComponent.y
                    ),
                    ((IEntity) entity).getWorld(),
                    connectionComponent.manager
            );
        }
    }

    private void processVelocity(Entity entity, BodyComponent bodyComponent) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        if (velocityComponent.x != 0 || velocityComponent.y != 0) {
            bodyComponent.body.setLinearVelocity(velocityComponent.x, velocityComponent.y);
        }
    }

    private void processPosition(Entity entity, BodyComponent bodyComponent) {
        PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);

        positionComponent.x = bodyComponent.body.getPosition().x;
        positionComponent.y = bodyComponent.body.getPosition().y;

//        System.out.println(positionComponent);

        if (game instanceof IServerGame) {
            Vector2 position = new Vector2(positionComponent.x, positionComponent.y);

            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);

            IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);

            ((IServerGame) game).getConnectionHandler().broadcastPacket(
                    new EntityPositionServerPacket(identifiable.uuid, position),
                    ((IEntity) entity).getWorld(),
                    connectionComponent != null ? connectionComponent.manager : null
            );

            if (connectionComponent != null && MovementComponent.MAPPER.has(entity)) {
                MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);

                if (movementComponent.sequenceId != -1) {
                    connectionComponent.manager.sendPacket(new PlayerMovementResponseServerPacket(movementComponent.sequenceId, position));

                    movementComponent.sequenceId = -1;
                }
            }
        }
    }
}
