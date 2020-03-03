package dev.game.test.core.setup.impl;

import dev.game.test.api.IGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.core.keybind.Keybinds;
import dev.game.test.core.registry.impl.KeybindsRegistry;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetupKeybinds implements Setup {

    private final IGame game;

    @Override
    public void setup() {
        KeybindsRegistry keybindsRegistry = this.game.getRegistryManager().getRegistry(Keybind.class);

        keybindsRegistry.registerKeybind(Keybinds.MOVE_UP);
        keybindsRegistry.registerKeybind(Keybinds.MOVE_LEFT);
        keybindsRegistry.registerKeybind(Keybinds.MOVE_DOWN);
        keybindsRegistry.registerKeybind(Keybinds.MOVE_RIGHT);
    }
}
