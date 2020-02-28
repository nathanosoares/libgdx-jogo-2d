package dev.game.test.core.net;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dev.game.test.api.net.IPacketHandler;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.core.CoreConstants;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketHandler implements IPacketHandler {

    protected final Connection connection;

    private final Multimap<Class<? extends Packet>, PacketListener> listeners = HashMultimap.create();

    private final ConcurrentLinkedQueue<Map.Entry<Packet, Long>> queue = new ConcurrentLinkedQueue<Map.Entry<Packet, Long>>();

    String debugTag;
    String debugColor;

    public PacketHandler(Connection connection, String debugTag, String debugColor) {
        this.connection = connection;
        this.debugColor = debugColor;
        this.debugTag = debugTag;

        loadListeners();
    }

    private void loadListeners() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            method.setAccessible(true);

            PacketEvent event = method.getAnnotation(PacketEvent.class);
            if (event == null) {
                continue;
            }

            Parameter[] parameters = method.getParameters();
            if (parameters.length != 1) {
                continue;
            }

            Parameter packetParameter = parameters[0];
            Class<?> packetClass = packetParameter.getType();
            if (!Packet.class.isAssignableFrom(packetClass)) {
                continue;
            }

            PacketListener packetListener = new PacketListener(method);
            listeners.put((Class<? extends Packet>) packetClass, packetListener);
        }

        for (Map.Entry<Class<? extends Packet>, Collection<PacketListener>> entry : listeners.asMap().entrySet()) {
            Gdx.app.log(getClass().getSimpleName(), String.format("Registered %d listeners for %s.", entry.getValue().size(), entry.getKey().getSimpleName()));
        }
    }

    public void queuePacket(Packet packet) {
        Gdx.app.debug(
                String.format("%s%s received packet\033[0m", debugColor, debugTag),
                String.format("%s%s\033[0m", debugColor, packet.getClass().getSimpleName())
        );

        queue.add(new AbstractMap.SimpleEntry<>(packet, System.currentTimeMillis()));
    }

    public void processQueue() {
        Iterator<Map.Entry<Packet, Long>> iterator = queue.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Packet, Long> entry = iterator.next();

            if (System.currentTimeMillis() - entry.getValue() > CoreConstants.INDUCED_LAG) {
                iterator.remove();

                for (PacketListener listener : listeners.get(entry.getKey().getClass())) {
                    try {
                        listener.method.invoke(this, entry.getKey());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     */

    public void sendPacket(Packet packet) {
        if (connection != null) {
            connection.sendUDP(packet);
        }
    }

    /*
     */

    @RequiredArgsConstructor
    private static class PacketListener {

        private final Method method;

    }

}