package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.client.PacketWorldReady;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketGameInfoResponse;
import dev.game.test.api.net.packet.server.PacketSpawnPosition;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.WorldUtils;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

public class PreparingListener extends AbstractPlayerPacketListener {


    public PreparingListener(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketGameInfoRequest packet) {
        if (this.connectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        // TODO enviar keybinds, texturas, etc..
        // Async?
        this.connectionManager.sendPacket(new PacketGameInfoResponse());
    }

    @Subscribe
    public void on(PacketGameInfoReady packet) {

        if (this.connectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        if (this.game.getServerManager().getWorlds().size() < 1) {
            this.connectionManager.getConnection().close();
            // TODO nenhum mundo para entrar
            return;
        }

        this.connectionManager.setPlayerUUID(UUID.randomUUID());

        Player player = PlayerUtils.buildLocalPlayer(
                this.connectionManager.getPlayerUUID(),
                this.connectionManager.getUsername()
        );

        // TODO mundar para config de default world
        IWorld world = this.game.getServerManager().getWorlds().get(0);

        player.setWorld(world);

        this.connectionManager.setPlayer(player);

        WorldUtils.sendWorld(this.connectionManager, world);
    }

    @Subscribe
    public void on(PacketWorldReady packet) {

        if (this.connectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        this.game.getEngine().addEntity(this.connectionManager.getPlayer());

        IWorld world = this.connectionManager.getPlayer().getWorld();

        Vector2 vector2 = world.getBounds().getCenter(new Vector2());
        this.connectionManager.sendPacket(new PacketSpawnPosition(world.getName(), vector2));

        this.connectionManager.setState(PacketConnectionState.State.INGAME);
        this.connectionManager.sendPacket(new PacketConnectionState(this.connectionManager.getState()));
    }
}
