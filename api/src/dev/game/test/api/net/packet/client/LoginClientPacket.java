package dev.game.test.api.net.packet.client;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginClientPacket implements Packet {

    @Getter
    private String username;

}
