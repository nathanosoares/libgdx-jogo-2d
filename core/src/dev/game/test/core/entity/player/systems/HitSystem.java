package dev.game.test.core.entity.player.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PlayerHitServerPacket;
import dev.game.test.core.Game;
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
            }
        }

        hitComponent.pending = false;
    }
}
