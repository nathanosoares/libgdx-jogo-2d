package dev.game.test.client.net.packet.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class PacketPlayerInfo {

    @Getter
    private UUID id;


}
