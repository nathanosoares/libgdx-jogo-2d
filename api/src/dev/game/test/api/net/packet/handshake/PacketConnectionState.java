package dev.game.test.api.net.packet.handshake;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PacketConnectionState implements Packet {

    @Getter
    private State state;

    @NoArgsConstructor
    @AllArgsConstructor
    public enum State {

        DISCONNECTED(-1),
        HANDSHAKE(0),
        PREPARING_INFO(1),
        PREPARING_WORLD(2),
        INGAME(3);

        @Getter
        private int id;
    }
}
