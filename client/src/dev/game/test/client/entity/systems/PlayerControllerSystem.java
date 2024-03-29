package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.ILivingEntity;
import dev.game.test.api.net.packet.client.DirectionClientPacket;
import dev.game.test.api.net.packet.client.HitClientPacket;
import dev.game.test.api.net.packet.client.MovementClientPacket;
import dev.game.test.client.entity.components.HitVisualComponent;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.components.GravityComponent;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.entity.components.TransformComponent;
import dev.game.test.core.entity.player.componenets.MovementComponent;
import dev.game.test.core.keybind.Keybinds;

public class PlayerControllerSystem extends EntitySystem {

    public static int sequenceNumber = 0;

    private final IClientGame game;

    public PlayerControllerSystem(IClientGame game) {
        this.game = game;
    }

    @Override
    public void update(float deltaTime) {
        Entity entity = (Entity) this.game.getClientManager().getPlayer();
        IEntity iEntity = (IEntity) entity;

        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        MovementComponent movementComponent = MovementComponent.MAPPER.get(entity);
        KeybindComponent activatedKeybinds = KeybindComponent.MAPPER.get(entity);
        HitVisualComponent hitVisualComponent = HitVisualComponent.MAPPER.get(entity);

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

        double oldDegrees = directionComponent.degrees;

        directionComponent.degrees = Math.toDegrees(Math.atan2(
                mouseWorldPosition.x - (transformComponent.x),
                mouseWorldPosition.y - (transformComponent.y)
        ));

        if (oldDegrees != directionComponent.degrees) {
            this.game.getConnectionHandler().getManager().sendPacket(new DirectionClientPacket(directionComponent.degrees));
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            hitVisualComponent.handler.pending = true;

            this.game.getConnectionHandler().getManager().sendPacket(new HitClientPacket());
        }

        if (movementComponent.deltaX != 0 || movementComponent.deltaY != 0) {

            this.game.getConnectionHandler().getManager().sendPacket(new MovementClientPacket(
                    sequenceNumber++,
                    movementComponent.deltaX,
                    movementComponent.deltaY
            ));
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            ((ILivingEntity) iEntity).damage(null, 0);
        }
    }
}
