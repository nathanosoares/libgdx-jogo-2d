package dev.game.test.client.net.handler.listeners;

import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.client.GameInfoRequestClientPacket;
import dev.game.test.api.net.packet.client.LoginClientPacket;
import dev.game.test.api.net.packet.client.WorldRequestClientPacket;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.client.screens.PreparingWorldScreen;
import org.greenrobot.eventbus.Subscribe;

public class ConnectionStatePacketListener extends AbstractServerPacketListener {

    public ConnectionStatePacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(PacketConnectionState packet) {

        this.manager.setState(packet.getState());

        switch (this.manager.getState()) {
            case HANDSHAKE:
                this.manager.sendPacket(new LoginClientPacket(this.game.getUsername()));
                break;
            case PREPARING_INFO:
                this.manager.sendPacket(new GameInfoRequestClientPacket());
                break;
            case PREPARING_WORLD:
                this.game.getEngine().removeAllEntities();

                this.game.getScreenManager().setCurrentScreen(new PreparingWorldScreen());

                this.manager.sendPacket(new WorldRequestClientPacket());
                break;
            case INGAME:
                IPlayer player = this.game.getClientManager().getPlayer();
                IWorld world = player.getWorld();

                this.game.getClientManager().setCurrentWorld(world);

                world.spawnEntity(player, player.getPosition());


                GameScreen screenGame = new GameScreen(this.game);

                this.game.getScreenManager().setCurrentScreen(screenGame);

                this.game.getConnectionHandler().getManager().registerListener(new EntityPacketListener(
                        this.game,
                        (ServerConnectionManager) this.game.getConnectionHandler().getManager()
                ));

                this.game.getConnectionHandler().getManager().registerListener(new PlayerMovementPacketListener(
                        this.game,
                        (ServerConnectionManager) this.game.getConnectionHandler().getManager()
                ));

                break;
            case DISCONNECTED:
                // close connection
                break;
            default:
                throw new RuntimeException("Unknown connection state: " + this.manager.getState());
        }
    }
}
