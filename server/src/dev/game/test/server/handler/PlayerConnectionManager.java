package dev.game.test.server.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
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
    public void queuePacket(Packet packet) {
//        Gdx.app.debug(
//                String.format("%s%s received packet\033[0m", "\033[1;33m", "Server"),
//                String.format("%s%s\033[0m", "\033[1;33m", packet.getClass().getSimpleName())
//        );

        super.queuePacket(packet);
    }

    @Override
    public void setState(PacketConnectionState.State state) {
        super.setState(state);

        this.sendPacket(new PacketConnectionState(this.getState()));
    }
}
