package dev.game.test.client.net.handler;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ServerConnectionManager implements IConnectionManager {

    private final Connection connection;

    @Setter
    private PacketConnectionState.State state = PacketConnectionState.State.DISCONNECTED;

    @Setter
    private ServerPacketListener packetListener;

    public void queuePacket(Packet packet) {
        if (packetListener != null) {
            packetListener.queuePacket(packet);
        }
    }
}
