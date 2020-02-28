package dev.game.test.client.net.handler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.client.PacketGameInfoReady;
import dev.game.test.api.net.packet.client.PacketGameInfoRequest;
import dev.game.test.api.net.packet.client.PacketLogin;
import dev.game.test.api.net.packet.client.PacketWorldReady;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.EmbeddedServerApplication;
import dev.game.test.client.GameUtils;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.core.net.PacketHandler;
import dev.game.test.core.world.World;

public class ServerPacketListener extends PacketHandler {

    private final IClientGame game;
    private final ServerConnectionManager connectionManager;

    public ServerPacketListener(IClientGame game, ServerConnectionManager connectionManager) {
        super(connectionManager.getConnection(), "Client", "\033[0;32m");

        this.game = game;
        this.connectionManager = connectionManager;
    }

    @Override
    public void sendPacket(Packet packet) {
        EmbeddedServerApplication serverApplication = ClientApplication.EMBEDDED_SERVER;

        if (packet != null) {
            serverApplication.getGame().getConnectionHandler().getConnectionManager(ClientApplication.DUMMY_CONNECTION)
                    .queuePacket(packet);
            return;
        }

        super.sendPacket(packet);
    }

    @PacketEvent
    public void on(PacketConnectionState packet) {

        this.connectionManager.setState(packet.getState());

        switch (this.connectionManager.getState()) {
            case HANDSHAKE:
                sendPacket(new PacketLogin(this.game.getUsername()));
                break;
            case PREPARING:
                sendPacket(new PacketGameInfoRequest());
                break;
            case INGAME:
                this.game.getEngine().addEntity((Entity) this.game.getClientManager().getPlayer());
                this.game.getClientManager().setCurrentWorld(this.game.getClientManager().getPlayer().getWorld());
                break;
            case DISCONNECTED:
                // close connection
                break;
            default:
                throw new RuntimeException("Unknown connection state: " + this.connectionManager.getState());
        }
    }

    @PacketEvent
    public void on(PacketLoginResponse packet) {
        this.game.getClientManager().setPlayer(GameUtils.buildClientPlayer(packet.getUuid(), this.game.getUsername()));
    }

    @PacketEvent
    public void on(PacketGameInfoResponse packet) {
        // TODO Load all game info. Download textures, register keybinds, etc...

        sendPacket(new PacketGameInfoReady());
    }

    @PacketEvent
    public void on(PacketWorldSnapshotFinish packet) {
        sendPacket(new PacketWorldReady());
    }

    @PacketEvent
    public void on(PacketWorldSnapshot packet) {
        World world = new World(packet.getWorldName(), packet.getLayersSize(), packet.getWidth(), packet.getHeight());

        game.getClientManager().addWorld(world);
    }

    @PacketEvent
    public void on(PacketWorldLayerSnapshot packet) {
        RegistryBlocks blockRegistry = game.getRegistryManager().getRegistry(Block.class);

        IWorld world = game.getClientManager().getWorld(packet.getWorldName());

        if (world == null) {
            Gdx.app.error("LayerDecode", String.format("World %s not found", packet.getWorldName()));
            return;
        }

        IWorldLayer worldLayer = world.getLayers()[packet.getLayerId()];

        PacketWorldLayerSnapshot.LayerData[][] dataArray = packet.getData();

        for (PacketWorldLayerSnapshot.LayerData[] layerData : dataArray) {
            for (PacketWorldLayerSnapshot.LayerData data : layerData) {
                if (data == null) {
                    continue;
                }

                Block block = blockRegistry.getBlock(data.getBlockId());

                if (block == null) {
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
    public void on(PacketSpawnPosition packet) {
        IWorld world = game.getClientManager().getWorld(packet.getWorldName());

        if (world == null) {
            Gdx.app.error("SpawnPosition", String.format("World %s not found", packet.getWorldName()));
            return;
        }
        IPlayer player = game.getClientManager().getPlayer();

        player.setWorld(world);
        player.setPosition(packet.getPosition());
    }

//    @PacketEvent
//    public void on(PacketEntitySpawn packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//
//        IPlayer player = world.getPlayer(packet.getId());
//        IClientManager clientManager = game.getClientManager();
//
//        if (packet.getId().equals(clientManager.getPlayer().getId())) {
//            player = clientManager.getPlayer();
//        } else if (player == null) {
//            player = new Player(packet.getId(), "");
//        }
//
//        if (!player.isSpawned()) {
//            world.spawnEntity(player, packet.getPosition());
//        }
//    }
//
//    @PacketEvent
//    public void on(PacketPlayerInfo packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IPlayer player = world.getPlayer(packet.getId());
//
//        if (player == null) {
//            return;
//        }
//
//        player.setName(packet.getName());
//    }
//
//
//    @PacketEvent
//    public void on(PacketEntityPosition packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IEntity entity = world.getEntity(packet.getId());
//
//        if (entity != null) {
//            /*if(!entity.equals(connectionHandler.clientManager.getPlayer())) {
//                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME);
//            } else {
//                entity.beginInterpolation(packet.getPosition(), Constants.INTERPOLATION_TIME * 4);
//            }*/
//        }
//    }
//
//    @PacketEvent
//    public void on(PacketEntityMovement packet) {
//        IWorld world = game.getClientManager().getCurrentWorld();
//        IPlayer player = world.getPlayer(packet.getId());
//
//        if (player != null) {
//            //player.move(packet.getMovement());
//        }
//    }
}
