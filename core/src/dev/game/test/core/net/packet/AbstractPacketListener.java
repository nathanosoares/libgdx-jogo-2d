package dev.game.test.core.net.packet;

import dev.game.test.api.IGame;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.IPacketListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractPacketListener<G extends IGame, M extends IConnectionManager> implements IPacketListener {

    protected final G game;
    protected final M manager;
}
