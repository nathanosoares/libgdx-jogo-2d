package dev.game.test.core.entity;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PacketEntityDestroy;
import dev.game.test.api.net.packet.server.PacketEntitySpawn;
import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.EntityComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.VelocityComponent;

import java.util.UUID;

public abstract class Entity extends com.badlogic.ashley.core.Entity implements IEntity {

    public Entity(UUID id) {
        this.add(new IdentifiableComponent(id));
        this.add(new PositionComponent(0, 0, null));
        this.add(new EntityComponent(false));
        this.add(new VelocityComponent());

        this.setupDefaultComponents();
    }

    protected void setupDefaultComponents() {

    }

    @Override
    public Vector2 getPosition() {
        PositionComponent position = PositionComponent.MAPPER.get(this);

        return new Vector2(position.x, position.y);
    }

    @Override
    public void setPosition(Vector2 vec) {
        PositionComponent position = PositionComponent.MAPPER.get(this);

        position.x = vec.x;
        position.y = vec.y;
    }

    @Override
    public void setPosition(IWorld world, Vector2 vec) {
        if (!world.equals(this.getWorld())) {
            world.spawnEntity(this, vec);
        } else {
            this.setPosition(vec);
        }
    }

    @Override
    public boolean isSpawned() {
        return EntityComponent.MAPPER.get(this).spawned;
    }

    @Override
    public void onSpawn(IWorld world) {
        EntityComponent.MAPPER.get(this).spawned = true;
        PositionComponent.MAPPER.get(this).world = world;

        if (Game.getInstance() instanceof IServerGame) {
            ((IServerGame) Game.getInstance()).getConnectionHandler().broadcastPacket(
                    new PacketEntitySpawn(this.getId(), getPosition()), world
            );
        }
    }

    @Override
    public void onDestroy(IWorld world) {
        EntityComponent.MAPPER.get(this).spawned = false;
        PositionComponent.MAPPER.get(this).world = null;

        if (Game.getInstance() instanceof IServerGame) {
            ((IServerGame) Game.getInstance()).getConnectionHandler().broadcastPacket(
                    new PacketEntityDestroy(this.getId()), world
            );
        }
    }

    /*

     */

    @Override
    public IWorld getWorld() {
        return PositionComponent.MAPPER.get(this).world;
    }

    @Override
    public UUID getId() {
        return IdentifiableComponent.MAPPER.get(this).uuid;
    }
}
