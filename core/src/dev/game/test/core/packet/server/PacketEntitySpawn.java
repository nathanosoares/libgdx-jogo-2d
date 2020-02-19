package dev.game.test.core.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.core.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketEntitySpawn implements Packet {

    @Getter
    private UUID id;

    @Getter
    private Vector2 position;

}