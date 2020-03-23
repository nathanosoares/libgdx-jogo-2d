package dev.game.test.core.entity.player.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PlayerHitServerPacket;
import dev.game.test.core.Box2dUtils;
import dev.game.test.core.Game;
import dev.game.test.core.entity.Hit;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.core.entity.player.componenets.HitComponent;

public class HitSystem extends IteratingSystem {

    private final Game game;

    public HitSystem(Game game) {
        super(Family.all(HitComponent.class, DirectionComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HitComponent hitComponent = HitComponent.MAPPER.get(entity);
        DirectionComponent directionComponent = DirectionComponent.MAPPER.get(entity);

        if (hitComponent.hitting) {
            hitComponent.time += deltaTime * 1000;

            if (hitComponent.time >= hitComponent.delay) {
                hitComponent.hitting = false;
            }
        } else if (hitComponent.pending) {
            hitComponent.hitting = true;
            hitComponent.time = 0;
            hitComponent.onRight = !hitComponent.onRight;
            hitComponent.degrees = directionComponent.degrees;

            if (game instanceof IServerGame) {
                ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get(entity);

                ((IServerGame) game).getConnectionHandler().broadcastPacket(
                        new PlayerHitServerPacket(
                                ((IEntity) entity).getId(),
                                hitComponent.pending,
                                hitComponent.delay,
                                hitComponent.time,
                                hitComponent.hitting,
                                hitComponent.onRight,
                                hitComponent.degrees
                        ),
                        ((IEntity) entity).getWorld(),
                        connectionComponent.manager
                );

                IEntity iEntity = (IEntity) entity;

                double radians = Math.toRadians(directionComponent.degrees);

                double x = iEntity.getWidth() * Math.sin(radians) + iEntity.getPosition().x + iEntity.getWidth() / 2;
                double y = iEntity.getHeight() * Math.cos(radians) + iEntity.getPosition().y + iEntity.getHeight() / 2;

                Hit hit = new Hit(iEntity, 2.0f);

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;

                float width = 1f;
                float height = 1.5f;

                ChainShape shape = Box2dUtils.createEllipse(width, height, 30);

                Body body = iEntity.getWorld().getBox2dWorld().createBody(bodyDef);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.isSensor = true;
                fixtureDef.filter.groupIndex = 1;

                Fixture fixture = body.createFixture(fixtureDef);
                fixture.setUserData(hit);

                body.setTransform((float) x, (float) y, (float) -radians);

                shape.dispose();

                Gdx.app.postRunnable(() -> iEntity.getWorld().getBox2dWorld().destroyBody(body));
            }
        }

        hitComponent.pending = false;
    }
}
