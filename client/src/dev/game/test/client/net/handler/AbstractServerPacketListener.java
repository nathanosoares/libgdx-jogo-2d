package dev.game.test.client.net.handler;

import dev.game.test.api.IClientGame;
import dev.game.test.api.net.IPacketListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractServerPacketListener  implements IPacketListener {

    protected final IClientGame game;
    protected final ServerConnectionManager connectionManager;

}
