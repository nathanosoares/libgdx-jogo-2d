package dev.game.test.api.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Container<O extends Identifiable> {

    private final Map<UUID, O> map = new HashMap<>();

    public void add(O object) {
        this.map.put(object.getId(), object);
    }

    public O get(UUID uuid) {
        return map.get(uuid);
    }

    public O remove(UUID uuid) {
        return map.remove(uuid);
    }

    public O remove(O object) {
        return map.remove(object.getId());
    }

    public Collection<O> getAll() {
        return map.values();
    }

}
