package dev.game.test.core.entity.player.componenets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.core.net.packet.AbstractConnectionManager;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConnectionComponent implements Component {

    public static final ComponentMapper<ConnectionComponent> MAPPER = ComponentMapper.getFor(ConnectionComponent.class);

    public AbstractConnectionManager manager;
}
