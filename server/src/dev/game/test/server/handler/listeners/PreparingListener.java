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
import dev.game.test.core.net.PacketEvent;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.PlayerPacketListener;
import dev.game.test.server.handler.WorldUtils;

import java.util.UUID;

public class PreparingListener extends PlayerPacketListener {

    public PreparingListener(IServerGame game, PlayerConnectionManager playerConnectionManager) {
        super(game, playerConnectionManager);
    }

    @PacketEvent
    public void on(PacketGameInfoRequest packet) {
        if (this.playerConnectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        // TODO enviar keybinds, texturas, etc..
        // Async?
        sendPacket(new PacketGameInfoResponse());
    }

    @PacketEvent
    public void on(PacketGameInfoReady packet) {

        if (this.playerConnectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        if (this.game.getServerManager().getWorlds().size() < 1) {
            this.playerConnectionManager.getConnection().close();
            // TODO nenhum mundo para entrar
            return;
        }

        this.playerConnectionManager.setPlayerUUID(UUID.randomUUID());

        Player player = PlayerUtils.buildLocalPlayer(
                this.playerConnectionManager.getPlayerUUID(),
                this.playerConnectionManager.getUsername()
        );

        // TODO mundar para config de default world
        IWorld world = this.game.getServerManager().getWorlds().get(0);

        player.setWorld(world);

        this.playerConnectionManager.setPlayer(player);

        WorldUtils.sendWorld(this, world);
    }

    @PacketEvent
    public void on(PacketWorldReady packet) {

        if (this.playerConnectionManager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        this.game.getEngine().addEntity(this.playerConnectionManager.getPlayer());

        IWorld world = this.playerConnectionManager.getPlayer().getWorld();

        Vector2 vector2 = world.getBounds().getCenter(new Vector2());
        sendPacket(new PacketSpawnPosition(world.getName(), vector2));

        this.playerConnectionManager.setState(PacketConnectionState.State.INGAME);
        sendPacket(new PacketConnectionState(this.playerConnectionManager.getState()));
    }
}
