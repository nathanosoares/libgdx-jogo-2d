package dev.game.test.api.entity;

public interface IProjectile extends IEntity {

    IEntity getSource();

    void setSource(IEntity entity);
}
