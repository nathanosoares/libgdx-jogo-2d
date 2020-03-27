package dev.game.test.client.net.handler.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.net.packet.client.WorldReadyClientPacket;
import dev.game.test.api.net.packet.server.PlayerBreakAnimationServerPacket;
import dev.game.test.api.net.packet.server.WorldLayerSnapshotServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotFinishServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.client.entity.components.HitVisualComponent;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.client.registry.BlockRegistry;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.impl.BlockGrassPlant;
import dev.game.test.core.world.World;
import org.greenrobot.eventbus.Subscribe;

import java.util.AbstractMap;

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
        BlockRegistry blockRegistry = game.getRegistryManager().getRegistry(Block.class);

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

                IBlockState state = block.createState(world, worldLayer, data.getX(), data.getY());

                state.setConnectedData(data.getConnectedData());

                worldLayer.setBlockState(state);
            }
        }
    }

    @Subscribe
    public void on(WorldSnapshotFinishServerPacket packet) {
        this.manager.sendPacket(new WorldReadyClientPacket());
    }

    @Subscribe
    public void on(PlayerBreakAnimationServerPacket packet) {
        BlockRegistry blockRegistry = Game.getInstance().getRegistryManager().getRegistry(Block.class);

        IBlock block = blockRegistry.getBlock(packet.getBlockId());

        if (block instanceof BlockGrassPlant) {
            WorldRenderSystem system = game.getEngine().getSystem(WorldRenderSystem.class);

            if (system != null) {

                Texture texture = new Texture(Gdx.files.internal("grass_effect.png"));

                TextureRegion[] frames = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight())[0];

                Animation<TextureRegion> animation = new Animation<>(.1f, frames);

                system.getBreakAnimations().put(new AbstractMap.SimpleEntry<>(new GridPoint2(packet.getX(), packet.getY()), animation), 0f);
            }
        }
    }
}
