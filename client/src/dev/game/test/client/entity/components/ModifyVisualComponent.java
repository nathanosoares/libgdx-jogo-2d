package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.client.RenderInfo;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ModifyVisualComponent implements Component {

    public static final ComponentMapper<ModifyVisualComponent> MAPPER = ComponentMapper.getFor(ModifyVisualComponent.class);

    public final Consumer<RenderInfo> consumer;

}
