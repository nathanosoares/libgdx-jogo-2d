package dev.game.test.api.event.player;

import dev.game.test.api.entity.IPlayer;

public class PlayerJoinEvent extends PlayerEvent {

    public PlayerJoinEvent(IPlayer player) {
        super(player);
    }
}
