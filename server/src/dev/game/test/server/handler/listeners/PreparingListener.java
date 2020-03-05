package dev.game.test.server.handler.listeners;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketGameInfoResponse;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class PreparingListener extends AbstractPlayerPacketListener {

    public PreparingListener(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketGameInfoRequest packet) {
        // TODO enviar keybinds, texturas, etc..
        // Async?
        this.manager.sendPacket(new PacketGameInfoResponse());
    }

    @Subscribe
    public void on(PacketGameInfoReady packet) {

        if (this.game.getServerManager().getWorlds().size() < 1) {
            this.manager.getConnection().close();
            // TODO nenhum mundo para entrar
            return;
        }

        Player player = PlayerUtils.buildLocalPlayer(
                this.manager.getPlayerUUID(),
                this.manager.getUsername()
        );

        player.add(new ConnectionComponent(this.manager));

        // TODO mundar para config de default world
        IWorld world = this.game.getServerManager().getWorlds().get(0);

        player.setPosition(world, world.getBounds().getCenter(new Vector2()));

        this.manager.setPlayer(player);

        this.game.getEngine().addEntity(player);

        this.manager.unregisterListener(PreparingListener.class);

        this.manager.registerListener(new WorldListener(this.game, this.manager));
        this.manager.setState(PacketConnectionState.State.PREPARING_WORLD);
    }
}