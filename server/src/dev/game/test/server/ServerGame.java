package dev.game.test.server;

import com.badlogic.ashley.core.Engine;
import dev.game.test.api.IGameManager;
import dev.game.test.api.IServerGame;
import dev.game.test.core.Game;
import dev.game.test.server.handler.ServerConnectionHandler;
import dev.game.test.server.systems.ServerSystem;
import lombok.Getter;

public class ServerGame extends Game implements IServerGame {

    @Getter
    private final ServerManager serverManager;

    @Getter
    private final ServerConnectionHandler connectionHandler;

    public ServerGame() {
        this.serverManager = new ServerManager(this, this.engine);
        this.connectionHandler = new ServerConnectionHandler(this);

        this.getEngine().addSystem(new ServerSystem(this));
    }

    @Override
    public IGameManager getGameManager() {
        return this.getServerManager();
    }

    @Override
    public void setupEngine(Engine engine) {
        super.setupEngine(engine);
    }
}
