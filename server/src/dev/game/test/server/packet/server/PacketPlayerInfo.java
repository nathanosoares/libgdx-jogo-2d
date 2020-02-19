package dev.game.test.server.packet.server;

import dev.game.test.server.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayerInfo implements Packet {

    @Getter
    private UUID id;

    @Getter
    private String name;

}
