package dev.game.test.api.entity;

public interface ILivingEntity extends IEntity {

    void damage(IEntity entity, float damage);

    float getHealth();

    void setHealth(float amount);

    float getMaxHealth();

    void setMaxHealth(float amount);

    void kill();

    boolean isDeath();
}
