package dev.game.test.net.client;

import com.esotericsoftware.kryonet.Connection;
import dev.game.test.net.GameConnection;
import dev.game.test.net.packet.handshake.PacketHandshake;
import dev.game.test.net.packet.Packet;
import dev.game.test.net.packet.server.PacketEntitySpawn;
import dev.game.test.net.packet.server.PacketPlayerInfo;
import dev.game.test.net.packet.server.PacketWorldLayerData;
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
        if (packet instanceof PacketHandshake) {
            connectionHandler.onHandshake((PacketHandshake) packet);
        } else if (packet instanceof PacketPlayerInfo) {
            connectionHandler.onPlayerInfo((PacketPlayerInfo) packet);
        } else if (packet instanceof PacketWorldLayerData) {
            connectionHandler.onWorldLayerData((PacketWorldLayerData) packet);
        } else if (packet instanceof PacketEntitySpawn) {
            connectionHandler.onEntitySpawn((PacketEntitySpawn) packet);
        }
    }

    /*

     */
}
