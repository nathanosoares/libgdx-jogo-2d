package dev.game.test.api.entity;

import dev.game.test.api.inventory.IInventory;
import dev.game.test.api.keybind.Keybind;

public interface IPlayer extends ILivingEntity {

    String getName();

    void setName(String name);

    void addActiveKeybind(Keybind keybind);

    void removeActiveKeybind(Keybind keybind);

    boolean hasActiveKeybind(Keybind keybind);

    IInventory getInventory();
}
