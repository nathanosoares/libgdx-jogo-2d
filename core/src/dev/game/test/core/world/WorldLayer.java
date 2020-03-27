package dev.game.test.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import dev.game.test.api.IClientGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.block.IPhysicBlockState;
import dev.game.test.api.net.packet.server.WorldLayerSnapshotServerPacket;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.Game;
import dev.game.test.core.block.states.BlockState;
import lombok.Getter;

public class WorldLayer implements IWorldLayer {

    @Getter
    protected final IWorld world;

    @Getter
    private final int index;

    @Getter
    protected IBlockState[][] states;

    public WorldLayer(World world, int index) {
        this.world = world;
        this.index = index;

        this.states = new BlockState[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];
    }

    public IBlockState getBlockState(float x, float y) {
        return getBlockState((int) x, (int) y);
    }

    public IBlockState getBlockState(int x, int y) {
        if (x < 0 || y < 0 || this.states.length <= x || this.states[x].length <= y) {
            return null;
        }

        return states[x][y];
    }

    public IBlockState getFacingBlock(IBlockState blockState, EnumFacing... facings) {
        GridPoint2 position = blockState.getPosition();

        int x = position.x;
        int y = position.y;

        for (EnumFacing facing1 : facings) {
            x += facing1.getOffset().x;
            y += facing1.getOffset().y;
        }

        return getBlockState(x, y);
    }

    public void setBlockState(IBlockState blockState) {
        for (int additionalX = 0; additionalX < blockState.getBlock().getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockState.getBlock().getHeight(); additionalY++) {

                int x = blockState.getPosition().x + additionalX;
                int y = blockState.getPosition().y + additionalY;

                if (isOrigin(x, y)) {
                    IBlockState oldState = this.states[x][y];

                    if (oldState instanceof IPhysicBlockState) {
                        Gdx.app.postRunnable(() -> this.world.getBox2dWorld().destroyBody(((IPhysicBlockState) oldState).getBody()));
                    }
                }

                if (additionalX == 0 && additionalY == 0) {
                    if (blockState instanceof IPhysicBlockState) {
                        ((IPhysicBlockState) blockState).createPhysics(this.world.getBox2dWorld());
                        ((IPhysicBlockState) blockState).getBody().setUserData(blockState);
                    }
                }

                this.states[x][y] = blockState;

                if (Game.getInstance() instanceof IClientGame) {
                    blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
                }
            }
        }

        if (this.world.isLoaded() && Game.getInstance() instanceof IServerGame) {
            WorldLayerSnapshotServerPacket.LayerData[][] dataArray = new WorldLayerSnapshotServerPacket.LayerData[][]{
                    {
                            new WorldLayerSnapshotServerPacket.LayerData(
                                    blockState.getBlock().getId(),
                                    blockState.getPosition().x,
                                    blockState.getPosition().y,
                                    blockState.getConnectedData()
                            )
                    }
            };

            ((IServerGame) Game.getInstance()).getConnectionHandler().broadcastPacket(
                    new WorldLayerSnapshotServerPacket(world.getName(), this.index, dataArray),
                    this.getWorld()
            );
        }
    }

    public boolean isOrigin(int x, int y) {
        IBlockState block = getBlockState(x, y);

        return block != null && (block.getPosition().x == x && block.getPosition().y == y);
    }
}
