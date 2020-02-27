package dev.game.test.api.keybind;

import lombok.EqualsAndHashCode;

public interface Keybind {

    String getId();

    String getName();

    String getDescription();

    int getKey();
}
