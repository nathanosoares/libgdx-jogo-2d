package dev.game.test.api.util;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumFacing {

    NORTH(new Vector2(0.0f, 1.0f)),
    EAST(new Vector2(1.0f, 0.0f)),
    SOUTH(new Vector2(0.0f, -1.0f)),
    WEST(new Vector2(-1.0f, 0.0f));

    @Getter
    private final Vector2 offset;

    @Getter
    private EnumFacing opposite;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
    }


}
