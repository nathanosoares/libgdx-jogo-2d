package dev.game.test.client.net.handler.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.client.WorldReadyClientPacket;
import dev.game.test.api.net.packet.server.WorldLayerSnapshotServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotFinishServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockSolidState;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.block.impl.BlockStone;
import dev.game.test.core.world.World;
import org.greenrobot.eventbus.Subscribe;

public class WorldPacketListener extends AbstractServerPacketListener {

    public WorldPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void on(WorldSnapshotServerPacket packet) {
        World world = new World(packet.getWorldName(), packet.getLayersSize(), packet.getWidth(), packet.getHeight());

        game.getClientManager().addWorld(world);
    }

    @Subscribe
    public void on(WorldLayerSnapshotServerPacket packet) {
        RegistryBlocks blockRegistry = game.getRegistryManager().getRegistry(Block.class);

        IWorld world = game.getClientManager().getWorld(packet.getWorldName());

        if (world == null) {
            Gdx.app.error("LayerDecode", String.format("World %s not found", packet.getWorldName()));
            return;
        }

        IWorldLayer worldLayer = world.getLayers()[packet.getLayerId()];

        WorldLayerSnapshotServerPacket.LayerData[][] dataArray = packet.getData();

        for (WorldLayerSnapshotServerPacket.LayerData[] layerData : dataArray) {
            for (WorldLayerSnapshotServerPacket.LayerData data : layerData) {
                if (data == null) {
                    continue;
                }

                Block block = blockRegistry.getBlock(data.getBlockId());

                if (block == null) {
                    Gdx.app.error("LayerDecode", String.format("Invalid block id: %d", data.getBlockId()));
                    continue;
                }

                BlockState state;
                if (block instanceof BlockStone) {
                    state = new BlockSolidState(block, world, worldLayer, new Vector2(2, 4));
                } else {
                    state = new BlockState(block, world, worldLayer, data.getPosition());
                }

                state.setConnectedData(data.getConnectedData());

                worldLayer.setBlockState(state);
            }
        }
    }

    @Subscribe
    public void on(WorldSnapshotFinishServerPacket packet) {
        this.manager.sendPacket(new WorldReadyClientPacket());
    }
}
