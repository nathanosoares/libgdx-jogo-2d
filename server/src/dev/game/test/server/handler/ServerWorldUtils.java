package dev.game.test.server.handler;

import dev.game.test.api.block.IBlockState;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.server.WorldLayerSnapshotServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotServerPacket;
import dev.game.test.api.net.packet.server.WorldSnapshotFinishServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;

public class ServerWorldUtils {

    public static void sendWorld(PlayerConnectionManager manager, IWorld world) {
        Packet worldSnapshot = new WorldSnapshotServerPacket(world.getName(), world.getLayers().length, (int) world.getBounds().getWidth(), (int) world.getBounds().getHeight());
        manager.sendPacket(worldSnapshot);

        for (int layerIndex = 0; layerIndex < world.getLayers().length; layerIndex++) {

            IWorldLayer worldLayer = world.getLayers()[layerIndex];

            WorldLayerSnapshotServerPacket.LayerData[][] dataArray = new WorldLayerSnapshotServerPacket.LayerData[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];

            for (int x = 0; x < dataArray.length; x++) {
                for (int y = 0; y < dataArray[x].length; y++) {
                    IBlockState blockState = worldLayer.getBlockState(x, y);

                    if (blockState == null || !worldLayer.isOrigin(x, y)) {
                        continue;
                    }

                    WorldLayerSnapshotServerPacket.LayerData data = new WorldLayerSnapshotServerPacket.LayerData(
                            blockState.getBlock().getId(),
                            x,
                            y,
                            blockState.getConnectedData()
                    );

                    dataArray[x][y] = data;
                }
            }

            WorldLayerSnapshotServerPacket worldLayerSnapshot = new WorldLayerSnapshotServerPacket(world.getName(), layerIndex, dataArray);
            manager.sendPacket(worldLayerSnapshot);
        }

        manager.sendPacket(new WorldSnapshotFinishServerPacket(world.getName()));
    }


}
