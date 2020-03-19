package dev.game.test.api.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntitySpawnServerPacket implements Packet {

    private UUID entityId;

    private EnumEntityType entityType;

    private Vector2 position;

    private double direction;

}
