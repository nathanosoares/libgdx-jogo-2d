package dev.game.test.client.net.packet.client;

import dev.game.test.client.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldRequest implements Packet {

    @Getter
    private int viewDistance;

}
