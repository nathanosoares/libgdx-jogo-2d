package dev.game.test.api.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.util.EnumFacing;
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
    private UUID entityId;

    @Getter
    @Setter
    private Vector2 position;

    @Getter
    @Setter
    private EnumFacing facing;
}
