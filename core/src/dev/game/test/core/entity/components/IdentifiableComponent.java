package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.sun.corba.se.spi.ior.Identifiable;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class IdentifiableComponent implements Component {

    public static final ComponentMapper<IdentifiableComponent> MAPPER = ComponentMapper.getFor(IdentifiableComponent.class);

    //

    public UUID uuid;
}
