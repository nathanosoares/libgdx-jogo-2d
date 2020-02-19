package dev.game.test.server;


import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dev.game.test.api.APIConstants;
import dev.game.test.server.packet.Packet;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class PacketHandler {

    protected final Connection connection;

    //

    private final Multimap<Class<? extends Packet>, PacketListener> listeners = HashMultimap.create();

    /*

     */

    public PacketHandler(Connection connection) {
        this.connection = connection;

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

    private final List<Map.Entry<Packet, Long>> receivedQueue = Collections.synchronizedList(Lists.newLinkedList());

    public void queuePacket(Packet packet) {
        Gdx.app.debug(getClass().getSimpleName(), String.format("Received packet: %s", packet.getClass().getSimpleName()));

        receivedQueue.add(new AbstractMap.SimpleEntry<>(packet, System.currentTimeMillis()));
    }

    public void callQueue() {
        Iterator<Map.Entry<Packet, Long>> iterator = receivedQueue.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Packet, Long> entry = iterator.next();
            if (System.currentTimeMillis() - entry.getValue() > APIConstants.INDUCED_LAG) {
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
