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
public class PacketEntityState implements Packet {

    @Getter
    @Setter
    private UUID entityId;

    @Getter
    @Setter
    private State state;
}
