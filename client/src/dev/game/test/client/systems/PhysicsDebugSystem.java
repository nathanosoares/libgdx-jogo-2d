package dev.game.test.client.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import dev.game.test.api.IClientGame;
import dev.game.test.api.world.IWorld;

public class PhysicsDebugSystem extends EntitySystem {

    private IClientGame game;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private boolean enabled = true;

    public PhysicsDebugSystem(IClientGame game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;

        this.debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            enabled = !enabled;
        }

        if (enabled) {

            IWorld world = game.getClientManager().getCurrentWorld();

            if (world != null) {
                debugRenderer.render(world.getBox2dWorld(), camera.combined);
            }
        }
    }
}