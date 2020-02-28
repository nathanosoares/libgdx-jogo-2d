package dev.game.test.server.handler;

import dev.game.test.api.IServerGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.client.PacketKeybindActivate;
import dev.game.test.api.net.packet.client.PacketKeybindDeactivate;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.PlayerUtils;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.NetworkComponent;
import dev.game.test.core.net.PacketEvent;
import dev.game.test.core.registry.impl.RegistryKeybinds;

import java.util.UUID;

public class PlayerPacketListener0 extends PlayerPacketListener {

    private Player player;

    public PlayerPacketListener0(IServerGame game, PlayerConnectionManager playerConnectionManager) {
        super(game, playerConnectionManager);

        IWorld world = game.getServerManager().getWorlds().get(0);

        player = PlayerUtils.buildLocalPlayer(UUID.randomUUID(), world.getName());
        player.add(new NetworkComponent(this));
    }

    @PacketEvent
    public void onKeybindActivate(PacketKeybindActivate keybindActivate) {
        RegistryKeybinds keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindActivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.player.addActiveKeybind(keybind);
    }

    @PacketEvent
    public void onKeybindDeactivate(PacketKeybindDeactivate keybindDeactivate) {
        RegistryKeybinds keybindRegistry = game.getRegistryManager().getRegistry(Keybind.class);
        Keybind keybind = keybindRegistry.getKeybind(keybindDeactivate.getKeybindId());

        if (keybind == null) {
            return;
        }

        this.player.removeActiveKeybind(keybind);
    }

}
