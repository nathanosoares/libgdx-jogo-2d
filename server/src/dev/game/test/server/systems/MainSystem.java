package dev.game.test.server.systems;

import com.badlogic.ashley.systems.IntervalSystem;
import dev.game.test.api.IServerGame;
import dev.game.test.server.ServerConstants;

public class MainSystem extends IntervalSystem {

    private final IServerGame serverGame;

    public MainSystem(IServerGame serverGame) {
        super(ServerConstants.MAIN_LOOP_INTERVAL);

        this.serverGame = serverGame;
    }

    @Override
    protected void updateInterval() {
        this.serverGame.getConnectionHandler().processQueue();
    }
}
