package dev.game.test.net.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.game.test.net.handshake.PacketHandshake;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumPacket {


    HANDSHAKE{
        {
            register(1, PacketHandshake.class);
        }
    };

    protected void register(int id, Class<? extends Packet> packet) {
        registry.put(id, packet);
    }

    /*

     */

    private static final BiMap<Integer, Class<? extends Packet>> registry = HashBiMap.create();

    public static int getIdFromPacket(Class<? extends Packet> packet) {
        return registry.inverse().get(packet);
    }

    public static Class<? extends Packet> getPacketById(int id) {
        return registry.get(id);
    }

}
