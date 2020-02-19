package dev.game.test.api.event.player;

import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PlayerEvent implements Event {

    @Getter
    private final IPlayer player;
}
