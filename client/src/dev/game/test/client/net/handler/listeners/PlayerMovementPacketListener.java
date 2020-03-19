package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PlayerHitServerPacket;
import dev.game.test.api.net.packet.server.PlayerMovementResponseServerPacket;
import dev.game.test.client.entity.systems.PlayerControllerSystem;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.entity.player.componenets.HitComponent;
import org.greenrobot.eventbus.Subscribe;

public class PlayerMovementPacketListener extends AbstractServerPacketListener {

    public PlayerMovementPacketListener(IClientGame game, ServerConnectionManager manager) {
        super(game, manager);
    }

    @Subscribe
    public void on(PlayerMovementResponseServerPacket packet) {
        if (packet.getSequenceNumber() == PlayerControllerSystem.sequenceNumber - 1) {
            this.game.getClientManager().getPlayer().setPosition(packet.getPosition());
        }
    }

    @Subscribe
    public void on(PlayerHitServerPacket packet) {

        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null && HitComponent.MAPPER.has((Entity) entity)) {
            HitComponent hitComponent = HitComponent.MAPPER.get((Entity) entity);

            hitComponent.pending = packet.isPending();
            hitComponent.delay = packet.getDelay();
            hitComponent.time = packet.getTime();
            hitComponent.hitting = packet.isHitting();
            hitComponent.onRight = packet.isOnRight();
            hitComponent.degrees = packet.getDegrees();
        }
    }
}
