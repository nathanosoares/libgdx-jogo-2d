package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.api.util.EnumFacing;

import java.util.EnumSet;

public class FacingVisualFlipComponent implements Component {

    public static final ComponentMapper<FacingVisualFlipComponent> MAPPER = ComponentMapper.getFor(FacingVisualFlipComponent.class);

    public EnumSet<EnumFacing> flipX = EnumSet.of(EnumFacing.WEST);
}
