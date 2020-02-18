package dev.game.test.client.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldLayerData {

    @Getter
    private String worldName;

    @Getter
    private int layerId;

    @Getter
    private LayerData data[];

    @Getter
    public static class LayerData {

        private int blockId;

        private Vector2 position;

        private int connectedData;

    }
}
