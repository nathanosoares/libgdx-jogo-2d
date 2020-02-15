package dev.game.test.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketEntitySpawn {

    @Getter
    private UUID id;

    @Getter
    private Vector2 position;

}
