package dev.game.test.server;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import dev.game.test.core.GameApplication;
import dev.game.test.server.components.ServerComponent;

public class ServerApplication extends GameApplication<ServerGame> {

    public ServerApplication() {
        super(ServerGame.class);
    }

    @Override
    public void create() {
        super.create();

        getGame().getServerManager().loadWorlds();
    }

}
