package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.World;
import lombok.Getter;

@Getter
public class BlockData {

    private World world;

    private Vector2 position;

    private int width;

    private int height;

}
