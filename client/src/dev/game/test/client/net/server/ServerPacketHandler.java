package dev.game.test.client.net.server;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.client.net.ConnectionHandler;
import dev.game.test.client.net.GameConnection;
import dev.game.test.client.net.packet.Packet;
import dev.game.test.client.net.packet.handshake.PacketHandshake;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ServerPacketHandler implements GameConnection {

    private final ConnectionHandler connectionHandler;

    private final Connection connection;

    /*

     */

    private UUID clientId;

    /*

     */

    @Override
    public void sendPacket(Packet packet) {
        connection.sendTCP(packet);
    }

    public void callPacket(Packet packet) {
        if (packet instanceof PacketHandshake) {
            this.clientId = ((PacketHandshake) packet).clientId;

            sendPacket(packet);
        }
    }

    /*

     */
}
