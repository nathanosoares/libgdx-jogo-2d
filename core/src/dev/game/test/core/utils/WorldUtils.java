package dev.game.test.core.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.world.IWorld;

public class WorldUtils {

    public static void adjustCollision(IWorld world, Rectangle box, Vector2 velocity, Vector2 position) {

        int startX, startY, endX, endY;

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

            IBlockState found = findBlock(world, velocity.x < 0, startX, endX, startY, endY);

            if (found != null) {
                if (velocity.x > 0) {
                    position.x = Math.min(
                            found.getPosition().x - box.width - 0.01f,
                            box.x + velocity.x
                    );
                } else if (velocity.x < 0) {
                    position.x = Math.max(
                            found.getPosition().x + found.getBlock().getWidth(),
                            box.x + velocity.x
                    );
                }
            } else {
                position.x += velocity.x;
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

            IBlockState found = findBlock(world, velocity.y < 0, startX, endX, startY, endY);

            if (found != null) {
                if (velocity.y > 0) {
                    position.y = Math.min(
                            found.getPosition().y - box.height - 0.01f,
                            box.y + velocity.y
                    );
                } else if (velocity.y < 0) {
                    position.y = Math.max(
                            found.getPosition().y + found.getBlock().getHeight(),
                            box.y + velocity.y
                    );
                }
            } else {
                position.y += velocity.y;
            }
        }
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

                IBlockState blockState = world.getLayers()[1].getBlockState(x, y);

                if (blockState != null) {
                    return blockState;
                }
            }
        }

        return null;
    }
}
