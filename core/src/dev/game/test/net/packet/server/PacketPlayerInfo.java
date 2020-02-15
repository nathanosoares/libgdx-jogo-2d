package dev.game.test.net.packet.server;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

import java.util.UUID;

public class PacketPlayerInfo {

    @Getter
    private UUID id;

    @Getter
    private Vector2 position;

}
