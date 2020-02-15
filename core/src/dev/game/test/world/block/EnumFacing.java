package dev.game.test.world.block;

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

}
