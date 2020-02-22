package dev.game.test.api.net;

import dev.game.test.api.net.packet.Packet;

public interface IPacketHandler {

    void queuePacket(Packet packet);

    void processQueue();

    //

    void sendPacket(Packet packet);

}
