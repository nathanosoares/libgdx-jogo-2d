package dev.game.test.server;

import dev.game.test.api.IServerGame;
import dev.game.test.core.Game;
import dev.game.test.server.handler.ServerConnectionHandler;
import lombok.Getter;

public class ServerGame extends Game implements IServerGame {

    @Getter
    private final ServerManager serverManager;

    @Getter
    private final ServerConnectionHandler connectionHandler;

    public ServerGame() {
        this.serverManager = new ServerManager();
        this.connectionHandler = new ServerConnectionHandler(this);
    }

}
