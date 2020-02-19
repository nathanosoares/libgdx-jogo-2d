package dev.game.test.server.packet.client;

import dev.game.test.server.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldRequest implements Packet {

    @Getter
    private int viewDistance;

}
