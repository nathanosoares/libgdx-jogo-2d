package dev.game.test.net.packet.handshake;

import dev.game.test.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    public UUID clientId;

}
