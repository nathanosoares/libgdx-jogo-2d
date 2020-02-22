package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.game.test.api.IClientGame;
import dev.game.test.core.entity.components.MovementComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalEntityControllerSystem extends EntitySystem {

    private final IClientGame clientGame;

    @Override
    public void update(float deltaTime) {
        MovementComponent movement = MovementComponent.MAPPER.get((Entity) this.clientGame.getClientManager().getPlayer());

        float walkSpeed = 4;

        boolean moveLeft = false;
        boolean moveRight = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveLeft = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveRight = true;
        }

        if (moveLeft == moveRight) {
            movement.velocityX = 0;
        } else if (moveLeft) {
            movement.velocityX = -walkSpeed;
        } else {
            movement.velocityX = walkSpeed;
        }

        boolean moveTop = false;
        boolean moveDown = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveTop = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveDown = true;
        }

        if (moveTop == moveDown) {
            movement.velocityY = 0;
        } else if (moveTop) {
            movement.velocityY = walkSpeed;
        } else {
            movement.velocityY = -walkSpeed;
        }
    }
}
