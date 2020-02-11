package dev.game.test.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.net.handshake.PacketHandshake;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameClientConnection implements GameConnection {

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
            System.out.println("SV - Handshake");
            Gdx.app.log("PACKET", "Received handshake from client!");
            this.clientId = ((PacketHandshake) packet).clientId;

            sendPacket(packet);
        }
    }

    /*

     */
}
