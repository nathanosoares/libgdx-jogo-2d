package dev.game.test.core.keybind;

import dev.game.test.api.keybind.Keybind;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KeybindAdapter implements Keybind {

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private final String description;

    @Getter
    private final int key;

}