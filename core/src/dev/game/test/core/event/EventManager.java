package dev.game.test.core.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.game.test.api.event.Event;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventManager {

    private final Multimap<Class<? extends Event>, EventSubscriber> subscribers = HashMultimap.create();

    public void callEvent(Event event) {
        for (EventSubscriber subscriber : subscribers.get(event.getClass())) {
            subscriber.dispatch(event);
        }
    }

    public void addSubscriber(EventSubscriber subscriber) {
        for (Class<? extends Event> eventClass : subscriber.getEventClasses()) {
            subscribers.put(eventClass, subscriber);
        }
    }

    public void removeSubscriber(EventSubscriber subscriber) {
        for (Class<? extends Event> eventClass : subscriber.getEventClasses()) {
            subscribers.remove(eventClass, subscriber);
        }
    }

}
