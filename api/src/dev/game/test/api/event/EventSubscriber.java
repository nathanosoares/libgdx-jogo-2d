package dev.game.test.api.event;

import dev.game.test.api.event.Event;

public interface EventSubscriber {

    Class<? extends Event>[] getEventClasses();

    void dispatch(Event event);

}
