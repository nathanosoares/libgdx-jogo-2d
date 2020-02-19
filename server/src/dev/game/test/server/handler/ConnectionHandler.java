package dev.game.test.server.handler;

import dev.game.test.server.packet.Packet;

public abstract class ConnectionHandler {

    abstract void sendPacket(Packet packet);
}
