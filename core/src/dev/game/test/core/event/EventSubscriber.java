package dev.game.test.core.event;

import dev.game.test.api.event.Event;

public interface EventSubscriber {

    Class<? extends Event>[] getEventClasses();

    void dispatch(Event event);

}
