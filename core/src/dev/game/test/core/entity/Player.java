package dev.game.test.core.entity;

import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class Player implements IPlayer {


    public final int entityId;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isValid() {
        return entityId != -1;
    }

    @Override
    public IWorld getWorld() {
        return null;
    }

    @Override
    public void setWorld(IWorld world) {

    }

    @Override
    public UUID getId() {
        return null;
    }
}
