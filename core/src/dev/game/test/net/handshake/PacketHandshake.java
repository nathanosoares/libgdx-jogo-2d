package dev.game.test.net.handshake;

import dev.game.test.net.packet.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PacketHandshake implements Packet {

    @Getter
    private String clientId;

    @Override
    public void write(DataOutputStream buffer) throws IOException {
        buffer.writeUTF(clientId);
    }

    @Override
    public void read(DataInputStream buffer) throws IOException {
        this.clientId = buffer.readUTF();
    }
}
