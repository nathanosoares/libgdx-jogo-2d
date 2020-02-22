package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.api.net.IPacketHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NetworkComponent implements Component {

    public static final ComponentMapper<NetworkComponent> MAPPER = ComponentMapper.getFor(NetworkComponent.class);

    //

    public IPacketHandler packetHandler;

}
