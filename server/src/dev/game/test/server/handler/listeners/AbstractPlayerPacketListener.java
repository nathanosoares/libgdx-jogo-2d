package dev.game.test.server.handler.listeners;

import dev.game.test.api.IServerGame;
import dev.game.test.core.net.packet.AbstractPacketListener;
import dev.game.test.server.handler.PlayerConnectionManager;

public abstract class AbstractPlayerPacketListener extends AbstractPacketListener<IServerGame, PlayerConnectionManager> {

    public AbstractPlayerPacketListener(IServerGame game, PlayerConnectionManager manager) {
        super(game, manager);
    }
}
