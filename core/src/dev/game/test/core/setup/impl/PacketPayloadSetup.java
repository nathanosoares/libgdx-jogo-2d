package dev.game.test.core.setup.impl;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.game.test.api.IGame;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.net.packet.client.*;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.entity.player.PlayerState;
import dev.game.test.core.registry.impl.PacketPayloadSerializerRegistry;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class PacketPayloadSetup implements Setup {

    private final IGame game;

    @Override
    public void setup() {
        PacketPayloadSerializerRegistry registry = this.game.getRegistryManager().getRegistry(Serializer.class);

        // Packets
        registry.registerSerializer(1000, PacketHandshake.class);
        registry.registerSerializer(1020, PacketConnectionState.class);
        registry.registerSerializer(1021, PacketConnectionState.State.class);
        registry.registerSerializer(1030, LoginClientPacket.class);
        registry.registerSerializer(1040, LoginResponseServerPacket.class);
        registry.registerSerializer(1050, GameInfoRequestClientPacket.class);
        registry.registerSerializer(1051, GameInfoResponseServerPacket.class);
        registry.registerSerializer(1060, GameInfoReadyClientPacket.class);
        registry.registerSerializer(1080, WorldSnapshotServerPacket.class);
        registry.registerSerializer(1081, WorldSnapshotFinishServerPacket.class);
        registry.registerSerializer(1090, WorldLayerSnapshotServerPacket.class);
        registry.registerSerializer(1091, WorldLayerSnapshotServerPacket.LayerData.class);
        registry.registerSerializer(1092, WorldLayerSnapshotServerPacket.LayerData[].class);
        registry.registerSerializer(1093, WorldLayerSnapshotServerPacket.LayerData[][].class);
        registry.registerSerializer(1100, WorldRequestClientPacket.class);
        registry.registerSerializer(1101, WorldReadyClientPacket.class);
        registry.registerSerializer(1110, SpawnPositionServerPacket.class);
        registry.registerSerializer(1120, KeybindActivateClientPacket.class);
        registry.registerSerializer(1140, KeybindDeactivateClientPacket.class);
        registry.registerSerializer(1150, EntityPositionServerPacket.class);
        registry.registerSerializer(1160, EntityMovementServerPacket.class);
        registry.registerSerializer(1170, EntitySpawnServerPacket.class);
        registry.registerSerializer(1171, EntityDestroyServerPacket.class);
        registry.registerSerializer(1180, MovementClientPacket.class);
        registry.registerSerializer(1181, PlayerMovementResponseServerPacket.class);
        registry.registerSerializer(1190, EntityStateServerPacket.class);
        registry.registerSerializer(1200, HitClientPacket.class);
        registry.registerSerializer(1201, PlayerHitServerPacket.class);
        registry.registerSerializer(1210, DirectionClientPacket.class);
        registry.registerSerializer(1211, EntityDirectionServerPacket.class);

        // Simple Objects
        registry.registerSerializer(2000, Vector2.class);
        registry.registerSerializer(2001, EnumFacing.class);
        registry.registerSerializer(2002, PlayerState.class);
        registry.registerSerializer(2003, EnumEntityType.class);

        // Complex Objects
        registry.registerSerializer(3000, UUID.class, kryo -> new Serializer<UUID>() {
            @Override
            public void write(Kryo kryo, Output output, UUID object) {
                output.writeString(object.toString());
            }

            @Override
            public UUID read(Kryo kryo, Input input, Class<? extends UUID> type) {
                return UUID.fromString(input.readString());
            }
        });
    }
}
