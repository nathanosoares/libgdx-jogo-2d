package dev.game.test.server.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.core.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PlayerConnectionManager implements IConnectionManager {

    private final Connection connection;

    @Setter
    private String username;

    @Setter
    private UUID playerUUID;

    @Setter
    private Player player;

    @Setter
    private PacketConnectionState.State state = PacketConnectionState.State.DISCONNECTED;

    @Setter
    private PlayerPacketListener packetListener;

    public void queuePacket(Packet packet) {
        if (packetListener != null) {
            packetListener.queuePacket(packet);
        }
    }
}
