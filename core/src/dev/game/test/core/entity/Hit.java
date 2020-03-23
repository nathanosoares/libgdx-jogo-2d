package dev.game.test.core.entity;

import com.google.common.collect.Lists;
import dev.game.test.api.entity.IEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class Hit {

    @Getter
    protected final IEntity source;

    @Getter
    protected final float damage;

    @Getter
    protected List<UUID> damaged = Lists.newArrayList();
}
