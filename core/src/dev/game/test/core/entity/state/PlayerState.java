package dev.game.test.core.entity.state;


import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum PlayerState implements State<Entity> {

    IDLE,
    WALK,
    JUMP;

    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity) {
        System.out.println(this);
    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
