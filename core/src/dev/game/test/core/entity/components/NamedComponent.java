package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NamedComponent implements Component {

    public String name;
}
