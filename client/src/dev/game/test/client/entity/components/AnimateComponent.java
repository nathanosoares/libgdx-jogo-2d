package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Predicate;

public class AnimateComponent implements Component {

    public static final ComponentMapper<AnimateComponent> MAPPER = ComponentMapper.getFor(AnimateComponent.class);

    public final Map<Predicate<Entity>, Animation<TextureRegion>> animations = Maps.newHashMap();
}
