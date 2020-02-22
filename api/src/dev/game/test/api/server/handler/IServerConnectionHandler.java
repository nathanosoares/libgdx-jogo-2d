package dev.game.test.api.server.handler;

import java.io.IOException;

public interface IServerConnectionHandler {

    void start(int port) throws IOException;

    void processQueue();

}
