package dev.game.test.net.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.game.test.net.GameClientConnection;
import dev.game.test.net.GameNet;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

public class ServerGameNet implements GameNet {

    private List<GameClientConnection> clientConnections = Lists.newArrayList();

    /*

     */

    private ServerSocket serverSocket;

    private Thread acceptThread;

    public void start(int port) {
        ServerSocketHints hints = new ServerSocketHints();

        this.serverSocket = Gdx.net.newServerSocket(Protocol.TCP, port, hints);

        this.acceptThread = new Thread(this::receiveClient);
        this.acceptThread.start();
    }

    private void receiveClient() {
        SocketHints hints = new SocketHints();

        while(true) {
            try {
                Socket socket = serverSocket.accept(hints);
                GameClientConnection connection = new GameClientConnection(this, socket);
                connection.init();
                clientConnections.add(connection);
            } catch(GdxRuntimeException ignored){}
        }
    }

}
