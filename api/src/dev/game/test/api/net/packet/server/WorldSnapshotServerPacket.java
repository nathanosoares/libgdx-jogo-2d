package dev.game.test.api.net.packet.server;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class WorldSnapshotServerPacket implements Packet {

    @Getter
    private String worldName;

    @Getter
    private int layersSize;

    @Getter
    private int width;

    @Getter
    private int height;

}
