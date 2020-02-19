package dev.game.test.core.packet.client;

import dev.game.test.core.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PacketKeybindDeactivate implements Packet {

    @Getter
    private String keybindId;

}
