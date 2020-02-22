package dev.game.test.client.net.handler;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IClientGame;
import dev.game.test.api.client.IClientManager;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.client.PacketWorldJoin;
import dev.game.test.api.net.packet.client.PacketWorldRequest;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.client.ClientManager;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.entity.Player;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.core.net.PacketHandler;
import dev.game.test.core.world.World;

public class ClientPacketHandler extends PacketHandler {

    private final IClientGame clientGame;

    //

    private ClientConnectionState state = ClientConnectionState.HANDSHAKE;

    //

    public ClientPacketHandler(IClientGame clientGame, Connection connection) {
        super(connection);

        this.clientGame = clientGame;
    }

    /*

     */

    @Override
    public void sendPacket(Packet packet) {
        /*ServerManager serverManager = connectionHandler.application.getLocalServerManager();

        if (serverManager != null) {
            for (ServerPacketHandler packetHandler : serverManager.getConnectionHandler().getConnections().values()) {
                packetHandler.queuePacket(packet);
            }

            return;
        }*/

        super.sendPacket(packet);
    }

    @PacketEvent
    public void onHandshake(PacketHandshake packet) {
        Gdx.app.log(getClass().getSimpleName(), "Received handshake!");

        this.state = ClientConnectionState.PREPARING;

        sendPacket(new PacketWorldRequest(30));
    }

    @PacketEvent
    public void onWorldLayerData(PacketWorldSnapshot packet) {
        World world = new World(packet.getWorldName(), packet.getWidth(), packet.getHeight());

        clientGame.getClientManager().setCurrentWorld(world);
    }

    @PacketEvent
    public void onWorldLayerData(PacketWorldLayerSnapshot packet) {
        RegistryBlocks blockRegistry = clientGame.getRegistryManager().getRegistry(Block.class);

        IWorld world = clientGame.getClientManager().getCurrentWorld();
        IWorldLayer worldLayer = world.getLayers()[packet.getLayerId()];

        PacketWorldLayerSnapshot.LayerData[][] dataArray = packet.getData();

        for (int x = 0; x < dataArray.length; x++) {
            for (int y = 0; y < dataArray[x].length; y++) {
                PacketWorldLayerSnapshot.LayerData data = dataArray[x][y];
                if(data == null) {
                    continue;
                }

                Block block = blockRegistry.getBlock(data.getBlockId());

                if(block == null) {
                    Gdx.app.error("LayerDecode", String.format("Invalid block id: %d", data.getBlockId()));
                    continue;
                }

                BlockState state = new BlockState(block, world, worldLayer, data.getPosition());
                state.setConnectedData(data.getConnectedData());

                worldLayer.setBlockState(state);
            }
        }
    }

    @PacketEvent
    public void onWorldLoaded(PacketWorldLoaded packet) {
        IPlayer player = clientGame.getClientManager().getPlayer();
        PacketWorldJoin worldJoin = new PacketWorldJoin(player.getId(), player.getName());
        sendPacket(worldJoin);
    }

    @PacketEvent
    public void onEntitySpawn(PacketEntitySpawn packet) {
        IWorld world = clientGame.getClientManager().getCurrentWorld();

        IPlayer player = world.getPlayer(packet.getId());
        IClientManager clientManager = clientGame.getClientManager();

        if(packet.getId().equals(clientManager.getPlayer().getId())) {
            player = clientManager.getPlayer();
        } else if(player == null) {
            player = new Player(packet.getId(), "");
        }

        if (!player.isSpawned()) {
            world.spawnEntity(player, packet.getPosition());
        }
    }

    @PacketEvent
    public void onPlayerInfo(PacketPlayerInfo packet) {
        IWorld world = clientGame.getClientManager().getCurrentWorld();
        IPlayer player = world.getPlayer(packet.getId());

        if(player == null) {
            return;
        }

        player.setName(packet.getName());
    }

    @PacketEvent
    public void onPrepareFinished(PacketPrepareFinished packet) {
        this.state = ClientConnectionState.INGAME;
    }

    @PacketEvent
    public void onEntityPosition(PacketEntityPosition packet) {
        IWorld world = clientGame.getClientManager().getCurrentWorld();
        IEntity entity = world.getEntity(packet.getId());

        if(entity != null) {
            /*if(!entity.equals(connectionHandler.clientManager.getPlayer())) {
                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME);
            } else {
                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME * 4);
            }*/
        }
    }

    @PacketEvent
    public void onEntityMovement(PacketEntityMovement packet) {
        IWorld world = clientGame.getClientManager().getCurrentWorld();
        IPlayer player = world.getPlayer(packet.getId());

        if(player != null) {
            //player.move(packet.getMovement());
        }
    }
}
