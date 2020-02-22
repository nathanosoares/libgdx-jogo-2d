package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.FacingComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.world.World;

public class MovementSystem extends IteratingSystem {

    private Vector2 velocity = new Vector2();

    public MovementSystem() {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }


    public static Multimap<Integer, Integer> debug = HashMultimap.create();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = PositionComponent.MAPPER.get(entity);
        MovementComponent movement = MovementComponent.MAPPER.get(entity);

        if (!CollisiveComponent.MAPPER.has(entity)) {
            position.x += movement.velocityX * deltaTime;
            position.y += movement.velocityY * deltaTime;

            updateFacing(entity, movement);
            return;
        }

        velocity.set(movement.velocityX, movement.velocityY);
        velocity.scl(5);

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

            BlockState found = findBlock(position.world, velocity.x < 0, startX, endX, startY, endY);

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

            BlockState found = findBlock(position.world, velocity.y < 0, startX, endX, startY, endY);

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

        updateFacing(entity, movement);
    }

    private BlockState findBlock(World world, boolean reverse, int startX, int endX, int startY, int endY) {

        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);

        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        if (reverse) {
            for (int x = maxX; x >= minX; x--) {
                for (int y = maxY; y >= minY; y--) {
                    debug.put(x, y);

                    BlockState blockState = world.getLayers()[1].getBlockState(x, y);

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

                BlockState blockState = world.getLayers()[1].getBlockState(x, y);

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
