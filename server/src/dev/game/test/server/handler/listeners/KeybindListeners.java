package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.client.PacketKeybindActivate;
import dev.game.test.api.net.packet.client.PacketKeybindDeactivate;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.server.handler.PlayerConnectionManager;
import org.greenrobot.eventbus.Subscribe;

public class KeybindListeners extends AbstractPlayerPacketListener {


    public KeybindListeners(IServerGame game, PlayerConnectionManager connectionManager) {
        super(game, connectionManager);
    }

    @Subscribe
    public void onKeybindActivate(PacketKeybindActivate keybindActivate) {
        RegistryKeybinds keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindActivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.connectionManager.getPlayer().addActiveKeybind(keybind);
    }

    @Subscribe
    public void onKeybindDeactivate(PacketKeybindDeactivate keybindDeactivate) {
        RegistryKeybinds keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindDeactivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.connectionManager.getPlayer().removeActiveKeybind(keybind);
    }

}
