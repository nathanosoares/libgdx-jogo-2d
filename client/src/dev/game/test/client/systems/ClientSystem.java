package dev.game.test.client.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import dev.game.test.api.IClientGame;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientSystem extends EntitySystem {

    private final IClientGame clientGame;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        clientGame.getConnectionHandler().processQueue();
    }
}
