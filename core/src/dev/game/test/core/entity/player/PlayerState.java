package dev.game.test.core.entity.player;


import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import dev.game.test.core.entity.Entity;
import dev.game.test.core.entity.player.componenets.MovementComponent;
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

        System.out.println(movement.deltaX );
        System.out.println(movement.deltaY);
        System.out.println("==");
        switch (this) {
            case IDLE:
                if (movement.deltaX != 0 || movement.deltaY != 0) {
                    state.machine.changeState(WALK);
                }
                return;
            case WALK:
                if (movement.deltaX == 0 && movement.deltaY == 0) {
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
