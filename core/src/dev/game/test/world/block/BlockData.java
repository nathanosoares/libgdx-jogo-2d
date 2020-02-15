package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.World;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockData {

    private Block block;

    private World world;

    private Vector2 position;

    private int width;

    private int height;

}
