package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PacketEntityMovement;
import dev.game.test.api.net.packet.server.PacketEntityPosition;
import dev.game.test.api.net.packet.server.PacketEntitySpawn;
import dev.game.test.client.GameUtils;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.entity.Player;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class EntityPacketListener extends AbstractServerPacketListener {

    public EntityPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketEntityPosition packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {

            if (packet.getEntityId().equals(this.game.getClientManager().getPlayer().getId())) {


                Vector2 currentPosition = entity.getPosition();
                double delta = Math.pow(packet.getPosition().x - currentPosition.x, 2)
                        + Math.pow(packet.getPosition().y - currentPosition.y, 2);

                if (delta > 2) {
                    entity.setPosition(packet.getPosition());
                } else if (delta > 1.5) {
//                    entity.setPosition(entity.getPosition().lerp(packet.getPosition(), 1f / 40f));
                }
            } else {
                entity.setPosition(packet.getPosition());
            }
        }
    }

    @Subscribe
    public void on(PacketEntityMovement packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            entity.setVelocity(packet.getVelocity());
        }
    }

    @Subscribe
    public void on(PacketEntitySpawn packet) {
        if (packet.getEntityId().equals(this.game.getClientManager().getPlayer().getId())) {
            return;
        }

        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            this.game.getEngine().removeEntity((Entity) entity);
            this.game.getClientManager().removeEntity(entity);
        }

        Player player = GameUtils.buildClientPlayer(packet.getEntityId(), UUID.randomUUID().toString());

        player.setPosition(packet.getPosition());
        player.setWorld(this.game.getClientManager().getPlayer().getWorld());

        this.game.getEngine().addEntity(player);
        this.game.getClientManager().addEntity(player);
    }
}
