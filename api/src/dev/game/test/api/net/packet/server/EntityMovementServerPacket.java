package dev.game.test.api.net.packet.server;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityMovementServerPacket implements Packet {

    private UUID entityId;

    public float deltaX;
    public float deltaY;

    public float fromX;
    public float fromY;

}
