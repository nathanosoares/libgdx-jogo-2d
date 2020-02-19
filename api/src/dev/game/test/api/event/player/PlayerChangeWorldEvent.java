package dev.game.test.api.event.player;

import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;

@Getter
public class PlayerChangeWorldEvent extends PlayerEvent {

    private final IWorld world;

    public PlayerChangeWorldEvent(IPlayer player, IWorld world) {
        super(player);
        this.world = world;
    }
}
