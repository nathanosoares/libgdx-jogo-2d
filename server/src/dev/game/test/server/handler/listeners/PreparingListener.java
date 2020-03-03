package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.client.PacketWorldReady;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketEntitySpawn;
import dev.game.test.api.net.packet.server.PacketGameInfoResponse;
import dev.game.test.api.net.packet.server.PacketSpawnPosition;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.ServerConnectionHandler;
import dev.game.test.server.handler.WorldUtils;
import org.greenrobot.eventbus.Subscribe;

public class PreparingListener extends AbstractPlayerPacketListener {

    public PreparingListener(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketGameInfoRequest packet) {
        if (this.manager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        // TODO enviar keybinds, texturas, etc..
        // Async?
        this.manager.sendPacket(new PacketGameInfoResponse());
    }

    @Subscribe
    public void on(PacketGameInfoReady packet) {

        if (this.manager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        if (this.game.getServerManager().getWorlds().size() < 1) {
            this.manager.getConnection().close();
            // TODO nenhum mundo para entrar
            return;
        }

        Player player = PlayerUtils.buildLocalPlayer(
                this.manager.getPlayerUUID(),
                this.manager.getUsername()
        );

        // TODO mundar para config de default world
        IWorld world = this.game.getServerManager().getWorlds().get(0);

        player.setWorld(world);

        this.manager.setPlayer(player);

        WorldUtils.sendWorld(this.manager, world);
    }

    @Subscribe
    public void on(PacketWorldReady packet) {

        if (this.manager.getState() != PacketConnectionState.State.PREPARING) {
            return;
        }

        this.manager.unregisterListener(PreparingListener.class);
        this.manager.registerListener(new KeybindListeners(game, manager));

        this.game.getEngine().addEntity(this.manager.getPlayer());

        IWorld world = this.manager.getPlayer().getWorld();

        Vector2 vector2 = world.getBounds().getCenter(new Vector2());

        this.manager.getPlayer().setPosition(vector2);
        this.manager.getPlayer().setWorld(world);

        this.manager.sendPacket(new PacketSpawnPosition(world.getName(), vector2));

        this.manager.setState(PacketConnectionState.State.INGAME);

        this.game.getConnectionHandler().broadcastPacket(new PacketEntitySpawn(
                this.manager.getPlayer().getId(),
                vector2
        ));

        for (PlayerConnectionManager manager : ((ServerConnectionHandler) this.game.getConnectionHandler()).getConnections().values()) {
            if (manager.getState() == PacketConnectionState.State.INGAME) {
                this.manager.sendPacket(new PacketEntitySpawn(
                        manager.getPlayer().getId(),
                        manager.getPlayer().getPosition()
                ));
            }
        }
    }
}
