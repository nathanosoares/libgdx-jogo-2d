package dev.game.test.api.client.handler;

import dev.game.test.api.net.packet.Packet;

import java.io.IOException;

public interface IClientConnectionHandler {

    void connect(String hostname, int port) throws IOException;

    void queuePacket(Packet packet);

    void processQueue();

}
