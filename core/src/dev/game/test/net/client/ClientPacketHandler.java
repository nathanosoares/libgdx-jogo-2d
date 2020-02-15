package dev.game.test.net.client;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.net.GameConnection;
import dev.game.test.net.ConnectionHandler;
import dev.game.test.net.packet.handshake.PacketHandshake;
import dev.game.test.net.packet.Packet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientPacketHandler implements GameConnection {

    private final ClientConnectionHandler connectionHandler;

    //

    private final Connection connection;

    @Override
    public void sendPacket(Packet packet) {
       connection.sendTCP(packet);
    }

    public void callPacket(Packet packet) {
        if(packet instanceof PacketHandshake) {
            connectionHandler.onHandshake(packet);
        }
    }

    /*

     */
}
