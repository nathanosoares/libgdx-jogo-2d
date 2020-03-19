package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.client.KeybindActivateClientPacket;
import dev.game.test.api.net.packet.client.KeybindDeactivateClientPacket;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.world.IWorld;
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

        if (keybind == Keybinds.CHANGE_WORLD && !this.manager.getPlayer().hasActiveKeybind(keybind)) {
            IWorld world = this.manager.getPlayer().getWorld();

            if (world.getName().equalsIgnoreCase("world")) {
                world = this.game.getServerManager().getWorld("test");
            } else {
                world = this.game.getServerManager().getWorld("world");
            }

            this.manager.getPlayer().setPosition(world, this.manager.getPlayer().getPosition());

            this.manager.registerListener(new WorldListener(this.game, this.manager));
            this.manager.setState(PacketConnectionState.State.PREPARING_WORLD);
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
