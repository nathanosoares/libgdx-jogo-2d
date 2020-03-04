package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.client.PacketPlayerMovement;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.core.keybind.Keybinds;

public class PlayerMovementControllerSystem extends EntitySystem {

    public static int sequenceNumber = 0;

    private final IClientGame game;

    public PlayerMovementControllerSystem(IClientGame game) {
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        Entity entity = (Entity) this.game.getClientManager().getPlayer();

        MovementComponent movement = MovementComponent.MAPPER.get(entity);
        KeybindComponent activatedKeybinds = KeybindComponent.MAPPER.get(entity);

        boolean moveLeft = false;
        boolean moveRight = false;

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_LEFT)) {
            moveLeft = true;
        }

        if (activatedKeybinds.activeKeybinds.contains(Keybinds.MOVE_RIGHT)) {
            moveRight = true;
        }

        if (moveLeft == moveRight) {
            movement.deltaX = 0;
        } else if (moveLeft) {
            movement.deltaX = -deltaTime;
        } else {
            movement.deltaX = deltaTime;
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
            movement.deltaY = 0;
        } else if (moveTop) {
            movement.deltaY = deltaTime;
        } else {
            movement.deltaY = -deltaTime;
        }

        if (movement.deltaX != 0 || movement.deltaY != 0) {
            this.game.getConnectionHandler().getManager().sendPacket(new PacketPlayerMovement(
                    sequenceNumber++,
                    movement.deltaX,
                    movement.deltaY
            ));
        }
    }
}
