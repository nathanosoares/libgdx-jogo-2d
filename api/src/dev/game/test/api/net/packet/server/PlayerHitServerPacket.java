package dev.game.test.api.net.packet.server;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerHitServerPacket implements Packet {

    private UUID entityId;

    private boolean pending = false;
    private float delay = 100f;
    private float time = 0;
    private boolean hitting = false;
    private boolean onRight = true;
    private double degrees = 0;

}
