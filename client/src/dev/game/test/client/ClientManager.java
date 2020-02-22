package dev.game.test.client;

import dev.game.test.api.client.IClientManager;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class ClientManager implements IClientManager {

    @Getter
    private final IPlayer player;

    @Setter
    @Getter
    private IWorld currentWorld;

}
