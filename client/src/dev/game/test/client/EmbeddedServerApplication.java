package dev.game.test.client;

import dev.game.test.server.AbstractServerApplication;


public class EmbeddedServerApplication extends AbstractServerApplication<EmbeddedServerGame> {

    private final ClientApplication hostApplication;

    public EmbeddedServerApplication(ClientApplication hostApplication) {
        super(EmbeddedServerGame.class);

        this.hostApplication = hostApplication;
    }

    @Override
    protected EmbeddedServerGame createGame() throws Exception {
        return new EmbeddedServerGame(hostApplication.getGame());
    }
}
