package dev.game.test.client.net.packet;

import com.google.common.collect.Maps;
import dev.game.test.client.net.packet.handshake.PacketHandshake;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public enum EnumPacket {


    HANDSHAKE {
        {
            register(1, PacketHandshake.class);
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
