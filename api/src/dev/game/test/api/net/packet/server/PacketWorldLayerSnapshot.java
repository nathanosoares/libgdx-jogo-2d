package dev.game.test.api.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldLayerSnapshot implements Packet {

    @Getter
    private String worldName;

    @Getter
    private int layerId;

    @Getter
    private LayerData data[][];

    @AllArgsConstructor
    @NoArgsConstructor
    public static class LayerData {

        @Getter
        private int blockId;

        @Getter
        private Vector2 position;

        @Getter
        private int connectedData;

    }
}
