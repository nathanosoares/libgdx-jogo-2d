package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BlockData {

    @Setter
    private Block block;

    private World world;

    private WorldLayer layer;

    private Vector2 position;

    private int width;

    private int height;

}
