package dev.game.test.core.packet.handshake;

import dev.game.test.core.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    public UUID clientId;

}
