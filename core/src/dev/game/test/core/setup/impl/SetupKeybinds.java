package dev.game.test.core.setup.impl;

import dev.game.test.api.IGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.core.keybind.Keybinds;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetupKeybinds implements Setup {

    private final IGame game;

    @Override
    public void setup() {
        RegistryKeybinds registryKeybinds = this.game.getRegistryManager().getRegistry(Keybind.class);

        registryKeybinds.registerKeybind(Keybinds.MOVE_UP);
        registryKeybinds.registerKeybind(Keybinds.MOVE_LEFT);
        registryKeybinds.registerKeybind(Keybinds.MOVE_DOWN);
        registryKeybinds.registerKeybind(Keybinds.MOVE_RIGHT);
    }
}
