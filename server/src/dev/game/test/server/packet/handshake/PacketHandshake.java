package dev.game.test.server.packet.handshake;

import dev.game.test.server.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    public UUID clientId;

}
