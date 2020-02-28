package dev.game.test.server.handler;

import dev.game.test.api.IEmbeddedServerGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.core.net.PacketHandler;

public abstract class PlayerPacketListener extends PacketHandler {

    protected final IServerGame game;
    protected final PlayerConnectionManager playerConnectionManager;

    public PlayerPacketListener(IServerGame game, PlayerConnectionManager playerConnectionManager) {
        super(playerConnectionManager.getConnection(), "Server", "\033[1;33m");

        this.game = game;
        this.playerConnectionManager = playerConnectionManager;
    }

    @Override
    public void sendPacket(Packet packet) {
        if (game instanceof IEmbeddedServerGame) {
            ((IEmbeddedServerGame) game).getHostGame().getConnectionHandler().queuePacket(packet);
            return;
        }

        super.sendPacket(packet);
    }
}
