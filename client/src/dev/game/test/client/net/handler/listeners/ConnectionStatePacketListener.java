package dev.game.test.client.net.handler.listeners;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.client.PacketLogin;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.client.screens.GameScreen;
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
            case PREPARING:
                this.manager.sendPacket(new PacketGameInfoRequest());
                break;
            case INGAME:
                this.game.getEngine().addEntity((Entity) this.game.getClientManager().getPlayer());

                this.game.getClientManager().setCurrentWorld(this.game.getClientManager().getPlayer().getWorld());

                GameScreen screenGame = new GameScreen(this.game);

                this.game.getScreenManager().setCurrentScreen(screenGame);

                this.game.getClientManager().addEntity(this.game.getClientManager().getPlayer());

                this.game.getConnectionHandler().getManager().registerListener(new EntityPacketListener(
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
