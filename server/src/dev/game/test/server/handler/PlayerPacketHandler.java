package dev.game.test.server.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IEmbeddedServerGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.client.PacketKeybindActivate;
import dev.game.test.api.net.packet.client.PacketKeybindDeactivate;
import dev.game.test.api.net.packet.client.PacketWorldJoin;
import dev.game.test.api.net.packet.client.PacketWorldRequest;
import dev.game.test.api.net.packet.handshake.PacketHandshake;
import dev.game.test.api.net.packet.server.*;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.NetworkComponent;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.core.net.PacketHandler;
import dev.game.test.core.registry.impl.RegistryKeybinds;

public class PlayerPacketHandler extends PacketHandler {

    private final IServerGame serverGame;

    private Player player;

    //

    public PlayerPacketHandler(IServerGame serverGame, Connection connection) {
        super(connection);

        this.serverGame = serverGame;
    }

    @Override
    public void sendPacket(Packet packet) {
        if (serverGame instanceof IEmbeddedServerGame) {
            ((IEmbeddedServerGame) serverGame).getHostGame().getConnectionHandler().queuePacket(packet);
            return;
        }

        super.sendPacket(packet);
    }

    @PacketEvent
    public void onHandshake(PacketHandshake handshake) {
        Gdx.app.log(getClass().getSimpleName(), "Received handshake!");

        sendPacket(handshake);
    }

    @PacketEvent
    public void onWorldRequest(PacketWorldRequest worldRequest) {
        // TODO criar evento para world padrão ou para spawn location
        IWorld world = serverGame.getServerManager().getWorlds().get(0);

        Packet worldSnapshot = new PacketWorldSnapshot(world.getName(), world.getLayers().length, (int) world.getBounds().getWidth(), (int) world.getBounds().getHeight());
        sendPacket(worldSnapshot);

        for (int layerIndex = 0; layerIndex < world.getLayers().length; layerIndex++) {

            IWorldLayer worldLayer = world.getLayers()[layerIndex];

            PacketWorldLayerSnapshot.LayerData[][] dataArray = new PacketWorldLayerSnapshot.LayerData[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];

            for (int x = 0; x < dataArray.length; x++) {
                for (int y = 0; y < dataArray[x].length; y++) {
                    IBlockState blockState = worldLayer.getBlockState(x, y);

                    if (blockState == null || !worldLayer.isOrigin(x, y)) {
                        continue;
                    }

                    PacketWorldLayerSnapshot.LayerData data = new PacketWorldLayerSnapshot.LayerData(blockState.getBlock().getId(), blockState.getPosition(), blockState.getConnectedData());
                    dataArray[x][y] = data;
                }
            }

            PacketWorldLayerSnapshot worldLayerSnapshot = new PacketWorldLayerSnapshot(world.getName(), layerIndex, dataArray);
            sendPacket(worldLayerSnapshot);
        }

        PacketSpawnPosition spawnPosition = new PacketSpawnPosition(
                world.getName(),
                world.getBounds().getCenter(new Vector2())
        );

        sendPacket(spawnPosition);
    }

    @PacketEvent
    public void onWorldJoin(PacketWorldJoin worldReady) {
        IWorld world = serverGame.getServerManager().getWorlds().get(0);

        player = new Player(worldReady.getId(), worldReady.getName());
        player.add(new NetworkComponent(this));

        world.spawnEntity(player, world.getBounds().getWidth() / 2.0f, world.getBounds().getHeight() / 2.0f);

        PacketPlayerInfo playerInfo = new PacketPlayerInfo(player.getId(), player.getName());

        for (IPlayer worldPlayer : world.getPlayers()) {
            worldPlayer.sendPacket(playerInfo);
        }

        PacketPrepareFinished prepareFinished = new PacketPrepareFinished();
        sendPacket(prepareFinished);
    }

    @PacketEvent
    public void onKeybindActivate(PacketKeybindActivate keybindActivate) {
        RegistryKeybinds keybindRegistry = serverGame.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindActivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.player.addActiveKeybind(keybind);
    }

    @PacketEvent
    public void onKeybindDeactivate(PacketKeybindDeactivate keybindDeactivate) {
        RegistryKeybinds keybindRegistry = serverGame.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindDeactivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.player.removeActiveKeybind(keybind);
    }

}
