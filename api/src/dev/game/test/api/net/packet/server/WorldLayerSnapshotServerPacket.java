package dev.game.test.api.net.packet.server;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class WorldLayerSnapshotServerPacket implements Packet {

    @Getter
    private String worldName;

    @Getter
    private int layerId;

    @Getter
    private LayerData[][] data;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LayerData {

        private int blockId;
        private int x;
        private int y;
        private int connectedData;

    }
}
