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
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.*;

public class MovementSystem extends IteratingSystem {

    private final Game game;

    private Vector2 positionCache = new Vector2();
    private Vector2 velocity = new Vector2();

    public MovementSystem(Game game) {
        super(Family.all(
                PositionComponent.class,
                MovementComponent.class
        ).get());

        this.game = game;
    }

    public static Multimap<Integer, Integer> debug = HashMultimap.create();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = PositionComponent.MAPPER.get(entity);
        MovementComponent movement = MovementComponent.MAPPER.get(entity);

        positionCache.set(position.x, position.y);

        if (!CollisiveComponent.MAPPER.has(entity)) {

            position.x += movement.velocityX * deltaTime;
            position.y += movement.velocityY * deltaTime;

        } else {

            velocity.set(movement.velocityX, movement.velocityY);

            Rectangle box = CollisiveComponent.MAPPER.get(entity).box;

            box.setPosition(position.x, position.y);

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

                IBlockState found = findBlock(position.world, velocity.x < 0, startX, endX, startY, endY);

                if (found != null) {
                    if (movement.velocityX > 0) {
                        position.x = Math.min(
                                found.getPosition().x - box.width - 0.01f,
                                position.x + movement.velocityX * deltaTime
                        );
                    } else if (movement.velocityX < 0) {
                        position.x = Math.max(
                                found.getPosition().x + found.getBlock().getWidth(),
                                position.x + movement.velocityX * deltaTime
                        );
                    }
                } else {
                    position.x += movement.velocityX * deltaTime;
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

                IBlockState found = findBlock(position.world, velocity.y < 0, startX, endX, startY, endY);

                if (found != null) {
                    if (movement.velocityY > 0) {
                        position.y = Math.min(
                                found.getPosition().y - box.height - 0.01f,
                                position.y + movement.velocityY * deltaTime
                        );
                    } else if (movement.velocityY < 0) {
                        position.y = Math.max(
                                found.getPosition().y + found.getBlock().getHeight(),
                                position.y + movement.velocityY * deltaTime
                        );
                    }
                } else {
                    position.y += movement.velocityY * deltaTime;
                }
            }
        }

//        System.out.println("== " + this.game.getClass().getSimpleName());
//        System.out.println(this.positionCache);
//        System.out.println(new Vector2(position.x, position.y));
//        System.out.println("==");

//        if (this.positionCache.x != position.x || this.positionCache.y != position.y) {
            updateFacing(entity, movement);

//            if (this.game instanceof IServerGame) {
//                IdentifiableComponent identifiable = IdentifiableComponent.MAPPER.get(entity);
//
//                ((IServerGame) this.game).getServerManager().broadcastPacket(new PacketEntityMovement(
//                        identifiable.uuid, new Vector2(position.x, position.y)
//                ));
//            }
//        }
    }

    private IBlockState findBlock(IWorld world, boolean reverse, int startX, int endX, int startY, int endY) {

        if (world == null || world.getLayers()[1] == null) {
            return  null;
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

    private void updateFacing(Entity entity, MovementComponent movement) {
        if (FacingComponent.MAPPER.has(entity)) {
            FacingComponent facing = FacingComponent.MAPPER.get(entity);

            if (movement.velocityX > 0) {
                facing.facing = EnumFacing.EAST;
            } else if (movement.velocityX < 0) {
                facing.facing = EnumFacing.WEST;
            }
        }
    }
}
