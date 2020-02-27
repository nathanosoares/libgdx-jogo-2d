package dev.game.test.api.server;

import dev.game.test.api.IGameManager;
import dev.game.test.api.net.packet.Packet;

public interface IServerManager extends IGameManager {

    void loadWorlds();

    void broadcastPacket(Packet packet);
}
