package dev.game.test.client.net.handler.listeners;

import dev.game.test.api.IClientGame;
import dev.game.test.client.net.handler.ServerConnectionManager;
import dev.game.test.core.net.packet.AbstractPacketListener;

public abstract class AbstractServerPacketListener extends AbstractPacketListener<IClientGame, ServerConnectionManager> {

    public AbstractServerPacketListener(IClientGame game, ServerConnectionManager manager) {
        super(game, manager);
    }
}
