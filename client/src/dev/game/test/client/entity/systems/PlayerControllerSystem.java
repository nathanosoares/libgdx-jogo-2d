package dev.game.test.client.entity.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.entity.components.RigidBodyComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerControllerSystem extends BaseSystem {

    private final GameScreen gameScreen;

    @Override
    protected void processSystem() {
        RigidBodyComponent rigidBodyComponent = this.world.getEntity(this.gameScreen.getPlayerId())
                .getComponent(RigidBodyComponent.class);

        float delta = this.world.getDelta();

        boolean moveLeft = false;
        boolean moveRight = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveLeft = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveRight = true;
        }

        if (moveLeft == moveRight) {
            rigidBodyComponent.velocity.x = 0;
        } else if (moveLeft) {
            rigidBodyComponent.velocity.x = -rigidBodyComponent.walkSpeed;
        } else {
            rigidBodyComponent.velocity.x = rigidBodyComponent.walkSpeed;
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
            rigidBodyComponent.velocity.y = 0;
        } else if (moveTop) {
            rigidBodyComponent.velocity.y = rigidBodyComponent.walkSpeed;
        } else {
            rigidBodyComponent.velocity.y = -rigidBodyComponent.walkSpeed;
        }
    }
}
