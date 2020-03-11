package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.game.test.api.IServerGame;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PacketEntityPosition;
import dev.game.test.api.net.packet.server.PacketPlayerMovementResponse;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.VelocityComponent;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;

public class VelocitySystem extends IteratingSystem {

    private final Game game;

    private final Vector2 toPosition = new Vector2();
    private final Vector2 velocity = new Vector2();

    public VelocitySystem(Game game) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());

        this.game = game;
    }

    public static Multimap<Integer, Integer> debug = HashMultimap.create();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);

        Vector2 fromPosition = new Vector2(positionComponent.x, positionComponent.y);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        if (this.game instanceof IServerGame) {
        }

        fromPosition.set(positionComponent.x, positionComponent.y);
        toPosition.set(fromPosition);

        velocity.set(velocityComponent.x * 0.2f, velocityComponent.y * 0.2f);


        if (!CollisiveComponent.MAPPER.has(entity)) {

            toPosition.x += velocityComponent.x;
            toPosition.y += velocityComponent.y;

        } else {

            Rectangle box = CollisiveComponent.MAPPER.get(entity).box;

            box.setPosition(fromPosition.x, fromPosition.y);

            int startX, startY, endX, endY;

            debug.clear();
            // Velocity X
            VELOCITY_X:
            {
                if (velocity.x > 0) {
                    startX = (int) box.x;
                    endX = (int) (box.x + box.width + velocity.x);
                } else if (velocity.x < 0) {
                    startX = (int) (box.x + velocity.x);
                    endX = (int) box.x;
                } else {
                    break VELOCITY_X;
                }

                startY = (int) (box.y);
                endY = (int) (box.y + box.height);

                IBlockState found = findBlock(positionComponent.world, velocity.x < 0, startX, endX, startY, endY);

                if (found != null) {
                    if (velocity.x > 0) {
                        toPosition.x = Math.min(
                                found.getPosition().x - box.width - 0.01f,
                                fromPosition.x + velocity.x
                        );
                    } else if (velocity.x < 0) {
                        toPosition.x = Math.max(
                                found.getPosition().x + found.getBlock().getWidth(),
                                fromPosition.x + velocity.x
                        );
                    }
                } else {
                    toPosition.x += velocity.x;
                }
            }

            // Velocity Y
            VELOCITY_Y:
            {
                if (velocity.y > 0) {
                    startY = (int) box.y;
                    endY = (int) (box.y + box.height + velocity.y);
                } else if (velocity.y < 0) {
                    startY = (int) (box.y + velocity.y);
                    endY = (int) box.y;
                } else {
                    break VELOCITY_Y;
                }

                startX = (int) box.x;
                endX = (int) (box.x + box.width);

                IBlockState found = findBlock(positionComponent.world, velocity.y < 0, startX, endX, startY, endY);

                if (found != null) {
                    if (velocity.y > 0) {
                        toPosition.y = Math.min(
                                found.getPosition().y - box.height - 0.01f,
                                fromPosition.y + velocity.y
                        );
                    } else if (velocity.y < 0) {
                        toPosition.y = Math.max(
                                found.getPosition().y + found.getBlock().getHeight(),
                                fromPosition.y + velocity.y
                        );
                    }
                } else {
                    toPosition.y += velocity.y;
                }
            }
        }

        // TODO adicionar evento

        ((IEntity) entity).setPosition(toPosition);

        if (game instanceof IServerGame) {
//            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);
//
//            if (velocityComponent.sequenceId != -1) {
//
//                if (connectionComponent != null) {
//                    connectionComponent.manager.sendPacket(
//                            new PacketPlayerMovementResponse(velocityComponent.sequenceId, toPosition)
//                    );
//
//                    velocityComponent.sequenceId = -1;
//                }
//            }

//            if (fromPosition.x != toPosition.x || fromPosition.y != toPosition.y) {
//                IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);
//
//                ((IServerGame) game).getConnectionHandler().broadcastPacket(
//                        new PacketEntityPosition(identifiable.uuid, toPosition),
//                        ((IEntity) entity).getWorld(),
//                        connectionComponent != null ? connectionComponent.manager : null
//                );
//            }
        }

        velocityComponent.x -= velocity.x;
        velocityComponent.y -= velocity.y;
    }

    private static IBlockState findBlock(IWorld world, boolean reverse, int startX, int endX, int startY, int endY) {

        if (world == null || world.getLayers()[1] == null) {
            return null;
        }

        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);

        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        if (reverse) {
            for (int x = maxX; x >= minX; x--) {
                for (int y = maxY; y >= minY; y--) {
                    debug.put(x, y);

                    IBlockState blockState = world.getLayers()[1].getBlockState(x, y);

                    if (blockState != null) {
                        return blockState;
                    }
                }
            }

            return null;
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                debug.put(x, y);

                IBlockState blockState = world.getLayers()[1].getBlockState(x, y);

                if (blockState != null) {
                    return blockState;
                }
            }
        }

        return null;
    }
}
