package dev.game.test.net.server;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.net.GameConnection;
import dev.game.test.net.GameNet;
import dev.game.test.net.handshake.PacketHandshake;
import dev.game.test.net.packet.Packet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerPacketHandler implements GameConnection {

    private final GameNet gameNet;

    private final Connection connection;

    /*

     */

    private String clientId;

    /*

     */

    @Override
    public void sendPacket(Packet packet) {
        connection.sendTCP(packet);
    }

    public void callPacket(Packet packet) {
        if(packet instanceof PacketHandshake) {
            this.clientId = ((PacketHandshake) packet).clientId;

            sendPacket(packet);
        }
    }

    /*

     */
}
