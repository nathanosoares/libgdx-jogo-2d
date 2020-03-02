package dev.game.test.server;

import java.io.IOException;

public class ServerApplication extends AbstractServerApplication<ServerGame> {

    public ServerApplication() {
        super(ServerGame.class);
    }

    @Override
    public void create() {
        super.create();

        try {
            getGame().getConnectionHandler().start(25565);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
