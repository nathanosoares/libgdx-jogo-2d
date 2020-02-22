package dev.game.test.core.entity;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.components.*;
import dev.game.test.core.entity.state.PlayerState;
import dev.game.test.core.world.World;

import java.util.UUID;

public class Player extends Entity implements IPlayer {

    public Player(String name, World world) {
        this.add(new NamedComponent(name));

        this.add(new PositionComponent(0, 0, world));
    }

    @Override
    protected void setupDefaultComponents() {
        super.setupDefaultComponents();

        this.add(new CollisiveComponent(24f / 16f, 24f / 16f));
        this.add(new MovementComponent());
        this.add(new FacingComponent(EnumFacing.EAST));

        DefaultStateMachine<Entity, PlayerState> defaultStateMachine = new DefaultStateMachine<>(this, PlayerState.WALK);

        this.add(new StateComponent<>(defaultStateMachine));
    }

    @Override
    public String getName() {
        return this.getComponent(NamedComponent.class).name;
    }

    @Override
    public boolean isValid() {
        return false;
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
