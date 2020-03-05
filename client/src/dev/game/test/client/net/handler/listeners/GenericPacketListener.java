package dev.game.test.client.net.handler.listeners;

import com.badlogic.gdx.Gdx;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.GameUtils;
import dev.game.test.client.net.handler.ServerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class GenericPacketListener extends AbstractServerPacketListener {

    public GenericPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketLoginResponse packet) {

        System.out.println(packet.getUuid());
        this.game.getClientManager().setPlayer(GameUtils.buildClientPlayer(packet.getUuid(), this.game.getUsername()));
    }

    @Subscribe
    public void on(PacketGameInfoResponse packet) {
        // TODO Load all game info. Download textures, register keybinds, etc...

        this.manager.sendPacket(new PacketGameInfoReady());
    }

    @Subscribe
    public void on(PacketSpawnPosition packet) {
        IWorld world = game.getClientManager().getWorld(packet.getWorldName());

        if (world == null) {
            Gdx.app.error("SpawnPosition", String.format("World %s not found", packet.getWorldName()));
            return;
        }

        IPlayer player = game.getClientManager().getPlayer();

        player.setPosition(world, packet.getPosition());
    }
}
