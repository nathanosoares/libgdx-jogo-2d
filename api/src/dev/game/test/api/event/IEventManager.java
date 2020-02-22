package dev.game.test.api.event;

public interface IEventManager {

    void callEvent(Event event);

    void addSubscriber(EventSubscriber subscriber);

    void removeSubscriber(EventSubscriber subscriber);

}
