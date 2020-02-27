package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.keybind.Keybinds;

public class PlayerMovementControllerSystem extends IteratingSystem {

    private final Game game;

    public PlayerMovementControllerSystem(Game game) {
        super(Family.all(KeybindComponent.class, MovementComponent.class).get());

        this.game = game;
    }

    public static Multimap<Integer, Integer> debug = HashMultimap.create();

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (this.game instanceof IClientGame) {
            IPlayer localPlayer = ((IClientGame) this.game).getClientManager().getPlayer();

            if (!entity.equals(localPlayer)) {
                return;
            }
        }

        MovementComponent movement = MovementComponent.MAPPER.get(entity);
        KeybindComponent activatedKeybinds = KeybindComponent.MAPPER.get(entity);

        float walkSpeed = 4;

        boolean moveLeft = false;
        boolean moveRight = false;

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_LEFT)) {
            moveLeft = true;
        }

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_RIGHT)) {
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

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_UP)) {
            moveTop = true;
        }

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_DOWN)) {
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
