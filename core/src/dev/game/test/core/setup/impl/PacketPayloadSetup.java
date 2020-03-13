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
        registry.registerSerializer(1030, PacketLogin.class);
        registry.registerSerializer(1040, PacketLoginResponse.class);
        registry.registerSerializer(1050, PacketGameInfoRequest.class);
        registry.registerSerializer(1051, PacketGameInfoResponse.class);
        registry.registerSerializer(1060, PacketGameInfoReady.class);
        registry.registerSerializer(1080, PacketWorldSnapshot.class);
        registry.registerSerializer(1081, PacketWorldSnapshotFinish.class);
        registry.registerSerializer(1090, PacketWorldLayerSnapshot.class);
        registry.registerSerializer(1091, PacketWorldLayerSnapshot.LayerData.class);
        registry.registerSerializer(1092, PacketWorldLayerSnapshot.LayerData[].class);
        registry.registerSerializer(1093, PacketWorldLayerSnapshot.LayerData[][].class);
        registry.registerSerializer(1100, PacketWorldRequest.class);
        registry.registerSerializer(1101, PacketWorldReady.class);
        registry.registerSerializer(1110, PacketSpawnPosition.class);
        registry.registerSerializer(1120, PacketKeybindActivate.class);
        registry.registerSerializer(1140, PacketKeybindDeactivate.class);
        registry.registerSerializer(1150, PacketEntityPosition.class);
        registry.registerSerializer(1160, PacketEntityMovement.class);
        registry.registerSerializer(1170, PacketEntitySpawn.class);
        registry.registerSerializer(1171, PacketEntityDestroy.class);
        registry.registerSerializer(1180, PacketPlayerMovement.class);
        registry.registerSerializer(1181, PacketPlayerMovementResponse.class);
        registry.registerSerializer(1190, PacketEntityState.class);
        registry.registerSerializer(1200, PacketHit.class);
        registry.registerSerializer(1210, PacketEntityDirection.class);

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
