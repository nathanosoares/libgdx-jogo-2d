package dev.game.test.core.net.packet;

import com.google.common.collect.Maps;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.client.PacketKeybindActivate;
import dev.game.test.api.net.packet.client.PacketKeybindDeactivate;
import dev.game.test.api.net.packet.client.PacketWorldJoin;
import dev.game.test.api.net.packet.client.PacketWorldRequest;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.api.net.packet.server.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public enum EnumPacket {


    HANDSHAKE {
        {
            register(1, PacketHandshake.class);
        }
    },
    PREPARING {
        {
            register(2, PacketWorldRequest.class);
            register(3, PacketWorldSnapshot.class);
            register(4, PacketWorldLayerSnapshot.class);
            register(5, PacketWorldLoaded.class);
            register(6, PacketWorldJoin.class);
            register(7, PacketPrepareFinished.class);
        }
    },
    INGAME {
        {
            register(8, PacketKeybindActivate.class);
            register(9, PacketKeybindDeactivate.class);
            register(10, PacketEntityPosition.class);
            register(11, PacketEntityMovement.class);
        }
    };

    protected void register(int id, Class<? extends Packet> packet) {
        if (registry == null) {
            registry = Maps.newHashMap();
        }

        registry.put(id, packet);
    }

    /*

     */

    public static Map<Integer, Class<? extends Packet>> registry;

}
