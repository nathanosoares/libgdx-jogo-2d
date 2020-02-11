package dev.game.test.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import dev.game.test.net.handshake.PacketHandshake;
import dev.game.test.net.packet.EnumPacket;
import dev.game.test.net.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameServerConnection implements GameConnection {

    private final GameNet gameNet;

    private final Socket socket;

    @Getter
    private String testString;

    /*

     */

    private Thread listenThread;

    public void init() {
        listenThread = new Thread(this::receivePacket);
        listenThread.start();

        sendPacket(new PacketHandshake("Hello World!"));
    }

    /*

     */

    protected void receivePacket() {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            while(true) {
                int packetId = dataInputStream.readInt();

                Class<? extends Packet> packetClass = EnumPacket.getPacketById(packetId);
                if(packetClass == null) {
                    continue;
                }

                Packet packet = packetClass.newInstance();
                packet.read(dataInputStream);
                callPacket(packet);
            }

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        try(DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
            dataOutputStream.writeInt(EnumPacket.getIdFromPacket(packet.getClass()));
            packet.write(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callPacket(Packet packet) {
        if(packet instanceof PacketHandshake) {
            Gdx.app.log("PACKET", "Received handshake from server!");
            this.testString = ((PacketHandshake) packet).getClientId();
        }
    }

    /*

     */
}
