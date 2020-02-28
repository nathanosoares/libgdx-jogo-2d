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

    @RequiredArgsConstructor
    public enum State {

        DISCONNECTED(-1),
        HANDSHAKE(0),
        PREPARING(1),
        INGAME(2);

        @Getter
        private final int id;
    }
}
