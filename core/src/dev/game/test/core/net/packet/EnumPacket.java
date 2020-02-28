package dev.game.test.core.net.packet;

import com.google.common.collect.Maps;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.client.*;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.api.net.packet.server.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public enum EnumPacket {

    DISCONNECTED {
        {
            register(1, PacketHandshake.class);
        }
    },
    HANDSHAKE {
        {
            register(2, PacketConnectionState.class);
            register(3, PacketLogin.class);
            register(4, PacketLoginResponse.class);
        }
    },
    PREPARING {
        {
            register(5, PacketGameInfoRequest.class);
            register(6, PacketGameInfoReady.class);
            register(8, PacketWorldSnapshot.class);
            register(9, PacketWorldLayerSnapshot.class);
            register(10, PacketSpawnPosition.class);
        }
    },
    INGAME {
        {
            register(13, PacketKeybindActivate.class);
            register(14, PacketKeybindDeactivate.class);
            register(15, PacketEntityPosition.class);
            register(16, PacketEntityMovement.class);
        }
    };

    protected void register(int id, Class<? extends Packet> packet) {
        if (registry == null) {
            registry = Maps.newHashMap();
        }

        registry.put(id, packet);
    }

    public static Map<Integer, Class<? extends Packet>> registry;

}
