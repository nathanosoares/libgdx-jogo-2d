package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.core.entity.player.componenets.HitComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HitVisualComponent implements Component {

    public static final ComponentMapper<HitVisualComponent> MAPPER = ComponentMapper.getFor(HitVisualComponent.class);

    public final HitComponent handler;
    public final Animation<TextureRegion> animation;
}
