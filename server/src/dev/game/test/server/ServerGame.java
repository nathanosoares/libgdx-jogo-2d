package dev.game.test.server;

import dev.game.test.api.IServerGame;
import dev.game.test.core.Game;
import lombok.Getter;

public class ServerGame extends Game implements IServerGame {

    @Getter
    private final ServerManager gameManager;

    public ServerGame() {
        this.gameManager = new ServerManager();
    }

}
