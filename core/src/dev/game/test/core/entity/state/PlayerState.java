package dev.game.test.core.entity.state;


import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import dev.game.test.core.entity.Entity;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.StateComponent;

public enum PlayerState implements State<Entity> {

    IDLE,
    WALK,
    RUNNING;

    @Override
    public void enter(Entity entity) {
    }

    @Override
    public void update(Entity entity) {
        StateComponent<PlayerState> state = StateComponent.MAPPER.get(entity);
        MovementComponent movement = MovementComponent.MAPPER.get(entity);

        switch (this) {
            case IDLE:
                if (movement.velocityX != 0 || movement.velocityY != 0) {
                    state.machine.changeState(WALK);
                }
                return;
            case WALK:
                if (movement.velocityX == 0 && movement.velocityY == 0) {
                    state.machine.changeState(IDLE);
                }
                return;
        }
    }

    @Override
    public void exit(Entity entity) {

    }

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }
}
