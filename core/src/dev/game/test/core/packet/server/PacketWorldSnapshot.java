package dev.game.test.core.packet.server;

import dev.game.test.core.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldSnapshot implements Packet {

    @Getter
    private String worldName;

    @Getter
    private int width;

    @Getter
    private int height;
}
