package dev.game.test.server.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IEmbeddedServerGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.core.entity.Player;
import dev.game.test.core.net.packet.AbstractConnectionManager;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class PlayerConnectionManager extends AbstractConnectionManager {

    private final IServerGame game;

    @Setter
    private String username;

    @Setter
    private UUID playerUUID;

    @Setter
    private Player player;

    public PlayerConnectionManager(IServerGame game, Connection connection) {
        super(connection);
        this.game = game;
    }

    @Override
    public void sendPacket(Packet packet) {
//        if (game instanceof IEmbeddedServerGame) {
//            ((IEmbeddedServerGame) game).getHostGame().getConnectionHandler().queuePacket(packet);
//            return;
//        }

        super.sendPacket(packet);
    }
}
