package dev.game.test.server.handler;

import dev.game.test.api.block.IBlockState;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.server.PacketWorldLayerSnapshot;
import dev.game.test.api.net.packet.server.PacketWorldSnapshot;
import dev.game.test.api.net.packet.server.PacketWorldSnapshotFinish;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;

public class WorldUtils {

    public static void sendWorld(PlayerConnectionManager connectionManager, IWorld world) {
        Packet worldSnapshot = new PacketWorldSnapshot(world.getName(), world.getLayers().length, (int) world.getBounds().getWidth(), (int) world.getBounds().getHeight());
        connectionManager.sendPacket(worldSnapshot);

        for (int layerIndex = 0; layerIndex < world.getLayers().length; layerIndex++) {

            IWorldLayer worldLayer = world.getLayers()[layerIndex];

            PacketWorldLayerSnapshot.LayerData[][] dataArray = new PacketWorldLayerSnapshot.LayerData[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];

            for (int x = 0; x < dataArray.length; x++) {
                for (int y = 0; y < dataArray[x].length; y++) {
                    IBlockState blockState = worldLayer.getBlockState(x, y);

                    if (blockState == null || !worldLayer.isOrigin(x, y)) {
                        continue;
                    }

                    PacketWorldLayerSnapshot.LayerData data = new PacketWorldLayerSnapshot.LayerData(
                            blockState.getBlock().getId(),
                            blockState.getPosition(),
                            blockState.getConnectedData()
                    );
                    
                    dataArray[x][y] = data;
                }
            }

            PacketWorldLayerSnapshot worldLayerSnapshot = new PacketWorldLayerSnapshot(world.getName(), layerIndex, dataArray);
            connectionManager.sendPacket(worldLayerSnapshot);
        }

        connectionManager.sendPacket(new PacketWorldSnapshotFinish(world.getName()));
    }
}
