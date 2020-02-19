package dev.game.test.server.packet.server;

import dev.game.test.server.packet.Packet;
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
