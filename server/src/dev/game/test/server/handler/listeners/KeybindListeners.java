package dev.game.test.server.handler.listeners;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.IServerGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.client.KeybindActivateClientPacket;
import dev.game.test.api.net.packet.client.KeybindDeactivateClientPacket;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.entity.components.GravityComponent;
import dev.game.test.core.keybind.Keybinds;
import dev.game.test.core.registry.impl.KeybindsRegistry;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class KeybindListeners extends AbstractPlayerPacketListener {

    public KeybindListeners(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void onKeybindActivate(KeybindActivateClientPacket keybindActivate) {
        KeybindsRegistry keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindActivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        if (!this.manager.getPlayer().hasActiveKeybind(keybind)) {
            IWorld world = this.manager.getPlayer().getWorld();

            if (keybind == Keybinds.CHANGE_WORLD) {

                if (world.getName().equalsIgnoreCase("world")) {
                    world = this.game.getServerManager().getWorld("test");
                } else {
                    world = this.game.getServerManager().getWorld("world");
                }

                this.manager.getPlayer().setPosition(world, this.manager.getPlayer().getPosition());

                this.manager.registerListener(new WorldListener(this.game, this.manager));
                this.manager.setState(PacketConnectionState.State.PREPARING_WORLD);
            } else if (keybind == Keybinds.ADD_GRASS_PLANT) {

                world.getLayers()[1].setBlockState(Blocks.GRASS_PLANT.createState(
                        world,
                        world.getLayers()[1],
                        (int) this.manager.getPlayer().getPosition().x,
                        (int) this.manager.getPlayer().getPosition().y

                ));
            }
        }

        this.manager.getPlayer().addActiveKeybind(keybind);
    }

    @Subscribe
    public void onKeybindDeactivate(KeybindDeactivateClientPacket keybindDeactivate) {
        KeybindsRegistry keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindDeactivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.manager.getPlayer().removeActiveKeybind(keybind);
    }

}
