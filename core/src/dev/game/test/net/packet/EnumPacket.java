package dev.game.test.net.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import dev.game.test.net.handshake.PacketHandshake;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumPacket {


    HANDSHAKE{
        {
            register(1, PacketHandshake.class);
        }
    };

    protected void register(int id, Class<? extends Packet> packet) {
        if(registry == null) {
            registry = Maps.newHashMap();
        }

        registry.put(id, packet);
    }

    /*

     */

    public static Map<Integer, Class<? extends Packet>> registry;

}
