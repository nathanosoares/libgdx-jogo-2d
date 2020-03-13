package dev.game.test.server.handler.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.client.PacketWorldReady;
import dev.game.test.api.net.packet.client.PacketWorldRequest;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.PacketEntitySpawn;
import dev.game.test.api.net.packet.server.PacketSpawnPosition;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.player.componenets.ConnectionComponent;
import dev.game.test.server.handler.PlayerConnectionManager;
import dev.game.test.server.handler.ServerWorldUtils;
import org.greenrobot.eventbus.Subscribe;

public class WorldListener extends AbstractPlayerPacketListener {

    public WorldListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }

    private void lock() {
        this.manager.unregisterListener(KeybindListeners.class);
        this.manager.unregisterListener(PlayerMovementListener.class);
    }

    private void unlock() {
        this.manager.unregisterListener(WorldListener.class);
        this.manager.registerListener(new KeybindListeners(game, manager));
        this.manager.registerListener(new PlayerMovementListener(game, manager));
    }

    @Subscribe
    public void on(PacketWorldRequest packet) {
        this.lock();

        ServerWorldUtils.sendWorld(this.manager, this.manager.getPlayer().getWorld());
    }

    @Subscribe
    public void on(PacketWorldReady packet) {
        IPlayer player = this.manager.getPlayer();
        IWorld world = player.getWorld();
        Vector2 position = player.getPosition();

        player.setPosition(world, position);

        this.unlock();

        this.sendPacket(new PacketSpawnPosition(world.getName(), position));
        this.manager.setState(PacketConnectionState.State.INGAME);

        // Enviando posição do jogador atual para todos
//        this.broadcastPacket(new PacketEntitySpawn(player.getId(), position), world, this.manager);

        // enviando posição de todos para o jogador atual
        for (IPlayer target : world.getPlayers()) {
            ConnectionComponent connectionComponent = ConnectionComponent.MAPPER.get((Entity) target);

            if (connectionComponent.manager != this.manager) {
                if (connectionComponent.manager.getState() == PacketConnectionState.State.INGAME) {
                    this.sendPacket(new PacketEntitySpawn(target.getId(), target.getType(), target.getPosition(), target.getDirection()));
                }
            }
        }
    }
}
