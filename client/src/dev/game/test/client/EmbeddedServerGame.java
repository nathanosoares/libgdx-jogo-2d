package dev.game.test.client;

import dev.game.test.api.IClientGame;
import dev.game.test.api.IEmbeddedServerGame;
import dev.game.test.server.ServerGame;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmbeddedServerGame extends ServerGame implements IEmbeddedServerGame {

    @Getter
    private final IClientGame hostGame;

}
