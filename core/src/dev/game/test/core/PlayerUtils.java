package dev.game.test.core;

import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.KeybindComponent;

import java.util.UUID;

public class PlayerUtils {


    public static Player buildLocalPlayer(UUID uuid, String userName) {
        Player player = new Player(uuid, userName);

        player.add(new KeybindComponent());

        return player;
    }
}
