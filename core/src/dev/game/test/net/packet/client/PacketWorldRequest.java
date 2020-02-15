package dev.game.test.net.packet.client;

import dev.game.test.net.packet.Packet;
import lombok.Getter;

public class PacketWorldRequest implements Packet {

    @Getter
    private int viewDistance;

}
