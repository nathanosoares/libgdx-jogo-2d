package dev.game.test.client.net;


import dev.game.test.client.net.packet.Packet;

public interface GameConnection {

    void sendPacket(Packet packet);

}
