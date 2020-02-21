package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.Maps;

import java.util.Map;

public class AnimateStateComponent implements Component {

    public final Map<State<? extends Entity>, Animation<TextureRegion>> animations = Maps.newHashMap();
}
