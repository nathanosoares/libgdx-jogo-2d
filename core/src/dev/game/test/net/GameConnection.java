package dev.game.test.net;


import dev.game.test.net.packet.Packet;

public interface GameConnection {

    void sendPacket(Packet packet);

}
