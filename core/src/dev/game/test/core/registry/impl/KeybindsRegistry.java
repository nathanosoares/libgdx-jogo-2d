package dev.game.test.core.registry.impl;

import com.google.common.collect.Maps;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.registry.IRegistry;

import java.util.Map;

public class KeybindsRegistry implements IRegistry<Keybind> {

    private final Map<String, Keybind> keybinds = Maps.newHashMap();

    private final Map<Integer, Keybind> keyMap = Maps.newHashMap();

    //

    public Keybind getKeybind(String id) {
        return this.keybinds.get(id);
    }

    public Keybind getKeybindFromKey(int key) {
        return this.keyMap.get(key);
    }

    public void registerKeybind(Keybind keybind) {
        this.keybinds.put(keybind.getId(), keybind);
        this.keyMap.put(keybind.getKey(), keybind);
    }

}
