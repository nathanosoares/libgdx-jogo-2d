package dev.game.test.api.net.packet.client;

import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectionClientPacket implements Packet {

    private double degrees;
}
