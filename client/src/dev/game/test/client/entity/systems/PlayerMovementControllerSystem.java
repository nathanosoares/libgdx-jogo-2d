package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.client.PacketPlayerMovement;
import dev.game.test.api.util.EnumDirection;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.player.componenets.DirectionComponent;
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

        PositionComponent positionComponent = PositionComponent.MAPPER.get(entity);
        MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);
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
            movementComponent.deltaX = 0;
        } else if (moveLeft) {
            movementComponent.deltaX = -deltaTime;
        } else {
            movementComponent.deltaX = deltaTime;
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
            movementComponent.deltaY = 0;
        } else if (moveTop) {
            movementComponent.deltaY = deltaTime;
        } else {
            movementComponent.deltaY = -deltaTime;
        }

        Vector2 mouseWorldPosition = this.game.getEngine().getSystem(WorldRenderSystem.class)
                .getMouseWorldPosition(new Vector2());

        DirectionComponent directionComponent = DirectionComponent.MAPPER.get(entity);
        CollisiveComponent collisiveComponent = CollisiveComponent.MAPPER.get(entity);

        double oldDegrees = directionComponent.degrees;

        Vector2 boxSize = collisiveComponent.box.getSize(new Vector2());

        directionComponent.degrees = Math.toDegrees(Math.atan2(
                mouseWorldPosition.x - (positionComponent.x + boxSize.x / 2),
                mouseWorldPosition.y - (positionComponent.y + boxSize.y / 2)
        ));

        if (movementComponent.deltaX != 0 || movementComponent.deltaY != 0 || oldDegrees != directionComponent.degrees) {

            this.game.getConnectionHandler().getManager().sendPacket(new PacketPlayerMovement(
                    sequenceNumber++,
                    movementComponent.deltaX,
                    movementComponent.deltaY,
                    directionComponent.degrees
            ));
        }
    }
}
