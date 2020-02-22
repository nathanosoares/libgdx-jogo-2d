package dev.game.test.core.keybind;

import com.badlogic.gdx.Input;
import dev.game.test.api.keybind.Keybind;

public class Keybinds {

    public static Keybind MOVE_UP = new KeybindAdapter("move_up", "Move up", "desc", Input.Keys.W);

    public static Keybind MOVE_LEFT = new KeybindAdapter("move_left", "Move left", "desc", Input.Keys.A);

    public static Keybind MOVE_DOWN = new KeybindAdapter("move_down", "Move down", "desc", Input.Keys.S);

    public static Keybind MOVE_RIGHT = new KeybindAdapter("move_right", "Move right", "desc", Input.Keys.D);

}