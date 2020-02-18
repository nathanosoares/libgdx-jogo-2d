package dev.game.test.client.net.packet.handshake;

import dev.game.test.client.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    public UUID clientId;

}
