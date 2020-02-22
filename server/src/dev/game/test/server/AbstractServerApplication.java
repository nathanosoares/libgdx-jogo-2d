package dev.game.test.server;

import dev.game.test.core.GameApplication;

public class AbstractServerApplication<G extends ServerGame> extends GameApplication<G> {

    public AbstractServerApplication(Class<G> gameClass) {
        super(gameClass);
    }

    @Override
    public void create() {
        super.create();

        getGame().getServerManager().loadWorlds();

        // getGame().getConnectionHandler().start();
    }

}
