package dev.game.test.core.entity.player;


import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import dev.game.test.api.IServerGame;
import dev.game.test.core.Game;
import dev.game.test.core.entity.Entity;
import dev.game.test.core.entity.components.StateComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;

public enum PlayerState implements State<Entity> {

    IDLE,
    WALK,
    RUNNING;

    @Override
    public void enter(Entity entity) {
    }

    @Override
    public void update(Entity entity) {
        MovementComponent movement = MovementComponent.MAPPER.get(entity);

        StateComponent<PlayerState> state = StateComponent.MAPPER.get(entity);

        switch (this) {
            case IDLE:
                if (movement.deltaX != 0 || movement.deltaY != 0) {
                    state.machine.changeState(WALK);
                }
                return;
            case WALK:
                if (Game.getInstance() instanceof IServerGame) {
                    if (movement.updatedAt > System.currentTimeMillis() - 40) {
                        return;
                    }
                }

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
