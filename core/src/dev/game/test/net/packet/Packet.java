package dev.game.test.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet {

    void write(DataOutputStream buffer) throws IOException;

    void read(DataInputStream buffer) throws IOException;

}
