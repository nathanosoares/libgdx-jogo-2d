package dev.game.test.core.entity.state;


import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import dev.game.test.core.entity.Entity;

public enum PlayerState implements State<Entity> {

    IDLE,
    WALK,
    RUNNING;

    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
