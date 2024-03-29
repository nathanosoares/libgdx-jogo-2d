package dev.game.test.api.net.packet.handshake;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    @Getter
    public String username;

}
