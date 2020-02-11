package dev.game.test.net.handshake;

import dev.game.test.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    public String clientId;
}
