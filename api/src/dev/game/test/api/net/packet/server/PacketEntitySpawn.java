package dev.game.test.api.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketEntitySpawn implements Packet {

    @Getter
    private UUID entityId;

    @Getter
    private Vector2 position;

}
