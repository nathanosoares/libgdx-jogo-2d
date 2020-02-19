package dev.game.test.api.event.player;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.IPlayer;
import lombok.Getter;

@Getter
public class PlayerMoveEvent extends PlayerEvent {

    private final Vector2 from;
    private final Vector2 to;

    public PlayerMoveEvent(IPlayer player, Vector2 from, Vector2 to) {
        super(player);
        this.from = from;
        this.to = to;
    }
}
