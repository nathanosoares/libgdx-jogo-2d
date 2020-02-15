package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumFacing {

    NORTH(new Vector2(0.0f, 1.0f), EnumFacing.SOUTH),
    EAST(new Vector2(1.0f, 0.0f), EnumFacing.WEST),
    SOUTH(new Vector2(0.0f, -1.0f), EnumFacing.NORTH),
    WEST(new Vector2(-1.0f, 0.0f), EnumFacing.EAST);

    @Getter
    private final Vector2 offset;

    @Getter
    private final EnumFacing opposite;

}
