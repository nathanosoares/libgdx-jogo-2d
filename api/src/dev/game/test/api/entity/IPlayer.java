package dev.game.test.api.entity;

import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.Packet;

public interface IPlayer extends IEntity {

    String getName();

    void setName(String name);

    /*
        Keybinds
     */

    void addActiveKeybind(Keybind keybind);

    void removeActiveKeybind(Keybind keybind);

    boolean hasActiveKeybind(Keybind keybind);

    /*
        Networking
     */

    void sendPacket(Packet packet);

    //
}
