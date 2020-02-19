package dev.game.test.server.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.server.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketEntityPosition implements Packet {

    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private Vector2 position;

}
