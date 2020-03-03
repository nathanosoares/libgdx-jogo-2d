package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.GameUtils;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.entity.Player;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class GenericPacketListener extends AbstractServerPacketListener {

    public GenericPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketEntityPosition packet) {
        IEntity entity = this.game.getClientManager().getEntity(packet.getEntityId());

        if (entity != null) {
            entity.setPosition(packet.getPosition());
        }
    }

    @Subscribe
    public void on(PacketEntitySpawn packet) {
        if (this.game.getConnectionHandler().getManager().getState() != PacketConnectionState.State.INGAME) {
            return;
        }

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

        this.game.getEngine().addEntity(player);
        this.game.getClientManager().addEntity(player);
    }

    @Subscribe
    public void on(PacketLoginResponse packet) {

        System.out.println(packet.getUuid());
        this.game.getClientManager().setPlayer(GameUtils.buildClientPlayer(packet.getUuid(), this.game.getUsername()));
    }

    @Subscribe
    public void on(PacketGameInfoResponse packet) {
        // TODO Load all game info. Download textures, register keybinds, etc...

        this.connectionManager.sendPacket(new PacketGameInfoReady());
    }

    @Subscribe
    public void on(PacketSpawnPosition packet) {
        IWorld world = game.getClientManager().getWorld(packet.getWorldName());

        if (world == null) {
            Gdx.app.error("SpawnPosition", String.format("World %s not found", packet.getWorldName()));
            return;
        }

        IPlayer player = game.getClientManager().getPlayer();

        player.setWorld(world);
        player.setPosition(packet.getPosition());
    }
}
