package dev.game.test.api.net.packet.server;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class EntityHeathServerPacket implements Packet {

    @Getter
    private UUID entityId;

    @Getter
    private float health;

    @Getter
    private float maxHealth;
}
