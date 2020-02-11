package dev.game.test.net.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import dev.game.test.net.GameNet;
import dev.game.test.net.GameServerConnection;

public class ClientGameNet implements GameNet {

    public GameServerConnection serverConnection;

    public void connect(String hostname, int port) {
        SocketHints hints = new SocketHints();

        Socket socket = Gdx.net.newClientSocket(Protocol.TCP, hostname, port, hints);
        this.serverConnection = new GameServerConnection(this, socket);
        this.serverConnection.init();
    }

}
