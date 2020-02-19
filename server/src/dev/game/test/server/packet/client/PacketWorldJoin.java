package dev.game.test.server.packet.client;

import dev.game.test.server.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketWorldJoin implements Packet {

    @Getter
    private UUID id;

    @Getter
    private String name;

}
