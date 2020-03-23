package dev.game.test.core.entity;

import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.ILivingEntity;
import dev.game.test.api.net.packet.server.EntityHeathServerPacket;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.HealthComponent;

import java.util.UUID;

public abstract class LivingEntity extends Entity implements ILivingEntity {

    public LivingEntity(UUID id) {
        super(id);
    }

    @Override
    public void damage(IEntity entity, float damage) {
        // TODO trigger no evento

        System.out.println("damage" + damage);
        setHealth(this.getHealth() - damage);

        if (Game.getInstance() instanceof IServerGame) {
            IServerGame game = (IServerGame) Game.getInstance();

            game.getConnectionHandler().broadcastPacket(new EntityHeathServerPacket(
                    this.getId(),
                    this.getHealth(), this.getMaxHealth()
            ), this.getWorld());
        }
    }

    @Override
    protected void setupDefaultComponents() {
        this.add(new HealthComponent());

    }

    @Override
    public void kill() {
        setHealth(0);
    }

    @Override
    public void setHealth(float amount) {
        HealthComponent.MAPPER.get(this).health = Math.min(Math.max(amount, 0), HealthComponent.MAPPER.get(this).maxHealth);
    }


    @Override
    public float getHealth() {
        return HealthComponent.MAPPER.get(this).health;
    }

    @Override
    public void setMaxHealth(float amount) {
        HealthComponent.MAPPER.get(this).maxHealth = Math.max(amount, 0);
    }

    @Override
    public float getMaxHealth() {
        return HealthComponent.MAPPER.get(this).maxHealth;
    }


    @Override
    public boolean isDeath() {
        return HealthComponent.MAPPER.get(this).health <= 0;
    }
}
