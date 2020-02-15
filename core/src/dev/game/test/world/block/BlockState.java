package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BlockState {

    @Setter
    private Block block;


    //

    private World world;

    private WorldLayer layer;

    //

    private Vector2 position;

    //

    public int connectedData;

    /*

     */

    public BlockState(Block block, World world, WorldLayer layer, Vector2 position) {
        this.block = block;
        this.world = world;
        this.layer = layer;
        this.position = position;
    }
}
