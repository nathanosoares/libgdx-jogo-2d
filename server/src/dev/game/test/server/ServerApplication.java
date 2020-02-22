package dev.game.test.server;

public class ServerApplication extends AbstractServerApplication<ServerGame> {

    public ServerApplication() {
        super(ServerGame.class);
    }

    @Override
    public void create() {
        super.create();

        getGame().getServerManager().loadWorlds();

        // getGame().getConnectionHandler().start();
    }

}
