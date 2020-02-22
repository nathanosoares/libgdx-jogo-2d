package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.google.common.collect.Sets;
import dev.game.test.api.keybind.Keybind;

import java.util.Collection;

public class KeybindComponent implements Component {

    public static final ComponentMapper<KeybindComponent> MAPPER = ComponentMapper.getFor(KeybindComponent.class);

    //

    public Collection<Keybind> activeKeybinds = Sets.newHashSet();
}
