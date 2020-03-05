package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.client.PacketLogin;
import dev.game.test.api.net.packet.client.PacketWorldRequest;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
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
                this.manager.sendPacket(new PacketLogin(this.game.getUsername()));
                break;
            case PREPARING_INFO:
                this.manager.sendPacket(new PacketGameInfoRequest());
                break;
            case PREPARING_WORLD:
                this.game.getEngine().removeAllEntities();

                this.game.getScreenManager().setCurrentScreen(new PreparingWorldScreen());

                this.manager.sendPacket(new PacketWorldRequest());
                break;
            case INGAME:
                this.game.getClientManager().addEntity(this.game.getClientManager().getPlayer());

                this.game.getClientManager().setCurrentWorld(this.game.getClientManager().getPlayer().getWorld());

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
