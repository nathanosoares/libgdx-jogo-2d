package dev.game.test.core.systems;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.Maps;
import dev.game.test.api.IGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.EntityMovementServerPacket;
import dev.game.test.api.net.packet.server.EntityPositionServerPacket;
import dev.game.test.api.net.packet.server.PlayerMovementResponseServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.block.impl.BlockWater;
import dev.game.test.core.entity.Entity;
import dev.game.test.core.entity.components.*;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;

import java.util.Map;


public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1 / 45f;
    private static float accumulator = 0f;

    private IGame game;

    private Array<Entity> bodiesQueue;

    private Map<Entity, Float> lastY = Maps.newHashMap();

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
            lastY.put(entity, entity.getPosition().y);

            BodyComponent bodyComponent = BodyComponent.MAPPER.get(entity);

            if (TransformComponent.MAPPER.has(entity)) {
                fixPosition(entity, bodyComponent);
            }

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

            BodyComponent bodyComponent = BodyComponent.MAPPER.get(entity);

            if (TransformComponent.MAPPER.has(entity)) {
                processPosition(entity, bodyComponent);
            }

            if (GravityComponent.MAPPER.has(entity)) {
                processGravity(entity);
            }
        }

        bodiesQueue.clear();
        lastY.clear();
    }

    @Override
    protected void processEntity(com.badlogic.ashley.core.Entity entity, float deltaTime) {
        if (BodyComponent.MAPPER.has(entity)) {
            bodiesQueue.add((Entity) entity);
        }
    }

    private final Vector2 movement = new Vector2();
    private final Vector2 gravityPosition = new Vector2();
    private final Vector2 entityPosition = new Vector2();
    private final Vector2 bodyPosition = new Vector2();

    private void fixPosition(Entity entity, BodyComponent bodyComponent) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

        bodyComponent.body.setTransform(new Vector2(
                transformComponent.x,
                transformComponent.y
        ), bodyComponent.body.getAngle());
    }


    private void processMovement(Entity entity, BodyComponent bodyComponent) {
        MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);

        float speed = movementComponent.speed * 50;

        entity.getPosition(entityPosition);

        IBlockState blockState = entity.getWorld().getLayers()[0].getBlockState(entityPosition.x, entityPosition.y);

        if (blockState != null && blockState.getBlock() instanceof BlockWater) {
            speed *= 0.3f;
        }

        movement.set(movementComponent.deltaX * speed, movementComponent.deltaY * speed);

        bodyComponent.body.setLinearVelocity(movement.x, movement.y);

        movementComponent.deltaX = 0;
        movementComponent.deltaY = 0;

        if (game instanceof IServerGame && ConnectionComponent.MAPPER.has(entity)) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);
            TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

            IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);

            ((IServerGame) game).getConnectionHandler().broadcastPacket(
                    new EntityMovementServerPacket(
                            identifiable.uuid,
                            movementComponent.deltaX,
                            movementComponent.deltaY,
                            transformComponent.x,
                            transformComponent.y
                    ),
                    ((IEntity) entity).getWorld(),
                    connectionComponent.manager
            );
        }
    }

    private void processVelocity(Entity entity, BodyComponent bodyComponent) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        velocityComponent.x = (float) Math.round(velocityComponent.x * 100f) / 100f;
        velocityComponent.y = (float) Math.round(velocityComponent.y * 100f) / 100f;

        if (velocityComponent.x != 0 || velocityComponent.y != 0) {
            bodyComponent.body.setLinearVelocity(velocityComponent.x, velocityComponent.y);

            velocityComponent.x -= 0.01 * Math.signum(velocityComponent.x);
            velocityComponent.y -= 0.01 * Math.signum(velocityComponent.y);
        }
    }

    private void processPosition(Entity entity, BodyComponent bodyComponent) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        GravityComponent gravityComponent = GravityComponent.MAPPER.get(entity);

        bodyPosition.set(bodyComponent.body.getPosition());

        transformComponent.x = bodyPosition.x;
        transformComponent.y = bodyPosition.y;

        if (transformComponent.altitude > 0) {
//            Vector2 gravityPosition = gravityComponent.body.getPosition();
//            gravityComponent.body.setTransform(
//                    transformComponent.x,
//                    gravityPosition.y - (lastY.get(entity) - transformComponent.y),
//                    gravityComponent.body.getAngle()
//
//            );
        }

        if (game instanceof IServerGame) {
            Vector2 position = new Vector2(transformComponent.x, transformComponent.y);

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

    private void processGravity(Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        GravityComponent gravityComponent = GravityComponent.MAPPER.get(entity);

        gravityPosition.set(gravityComponent.body.getPosition());

        entity.getPosition(entityPosition);

        if (gravityPosition.y < entityPosition.y) {
            gravityPosition.y = entityPosition.y;
        }

        gravityComponent.body.setTransform(
                entityPosition.x,
                gravityPosition.y,
                gravityComponent.body.getAngle()
        );

        transformComponent.altitude = gravityPosition.y - entityPosition.y;

        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        if (velocityComponent.x != 0f || velocityComponent.y != 0f) {

            if (transformComponent.altitude <= 0f) {
                velocityComponent.x = 0;
                velocityComponent.y = 0;
            }
        }
    }
}
