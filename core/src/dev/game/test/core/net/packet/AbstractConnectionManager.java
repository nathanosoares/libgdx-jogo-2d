package dev.game.test.core.net.packet;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.google.common.collect.Maps;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.net.IPacketListener;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.core.CoreConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.greenrobot.eventbus.EventBus;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@RequiredArgsConstructor
public class AbstractConnectionManager implements IConnectionManager {

    protected final Connection connection;

    @Setter
    protected PacketConnectionState.State state = PacketConnectionState.State.DISCONNECTED;

    private Map<Class<? extends IPacketListener>, IPacketListener> listenerMap = Maps.newConcurrentMap();

    private EventBus eventBus = EventBus.builder().build();

    private final ConcurrentLinkedQueue<Map.Entry<Packet, Long>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void sendPacket(Packet packet) {
        System.out.println("SendPacket " + this.getClass().getSimpleName());

        if (connection != null) {
            connection.sendTCP(packet);
        }
    }

    @Override
    public void queuePacket(Packet packet) {
        Gdx.app.debug(
                String.format("%s%s received packet\033[0m", "\033[1;33m", "Server"),
                String.format("%s%s\033[0m", "\033[1;33m", packet.getClass().getSimpleName())
        );

        queue.add(new AbstractMap.SimpleEntry<>(packet, System.currentTimeMillis()));
    }

    @Override
    public void processQueue() {
        Iterator<Map.Entry<Packet, Long>> iterator = queue.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Packet, Long> entry = iterator.next();

            if (System.currentTimeMillis() - entry.getValue() > CoreConstants.INDUCED_LAG) {
                iterator.remove();

                if (eventBus.hasSubscriberForEvent(entry.getKey().getClass())) {
                    eventBus.post(entry.getKey());
                }
            }
        }
    }

    @Override
    public void registerListener(IPacketListener listener) {
        unregisterListener(listener.getClass());

        eventBus.register(listener);
        listenerMap.put(listener.getClass(), listener);
    }

    @Override
    public void unregisterListener(Class<? extends IPacketListener> clazz) {
        if (listenerMap.containsKey(clazz)) {
            eventBus.unregister(listenerMap.get(clazz));
            listenerMap.remove(clazz);
        }
    }
}
