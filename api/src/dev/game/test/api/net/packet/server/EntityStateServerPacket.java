package dev.game.test.api.net.packet.server;

import com.badlogic.gdx.ai.fsm.State;
import dev.game.test.api.net.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class EntityStateServerPacket implements Packet {

    @Getter
    private UUID entityId;

    @Getter
    private State state;
}
