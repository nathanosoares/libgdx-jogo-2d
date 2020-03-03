package dev.game.test.client.systems;

import com.badlogic.ashley.core.EntitySystem;
import dev.game.test.api.IClientGame;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientSystem extends EntitySystem {

    private final IClientGame game;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        game.getConnectionHandler().getManager().processQueue();
    }

    @Override
    public boolean checkProcessing() {
        return this.game.getConnectionHandler().getManager() != null;
    }
}
