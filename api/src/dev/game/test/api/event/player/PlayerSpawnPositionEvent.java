package dev.game.test.api.event.player;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.IPlayer;
import lombok.Getter;

public class PlayerSpawnPositionEvent extends PlayerEvent {

    @Getter
    private Vector2 position;

    public PlayerSpawnPositionEvent(IPlayer player, Vector2 position) {
        super(player);

        this.position = position;
    }
}
