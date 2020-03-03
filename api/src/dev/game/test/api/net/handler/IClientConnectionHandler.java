package dev.game.test.api.net.handler;

import dev.game.test.api.net.IConnectionManager;

import java.io.IOException;

public interface IClientConnectionHandler {

    void connect(String hostname, int port) throws IOException;

    IConnectionManager getManager();

}
