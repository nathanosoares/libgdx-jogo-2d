package dev.game.test.core.inventory.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.core.inventory.Inventory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryComponent implements Component {

    public static final ComponentMapper<InventoryComponent> MAPPER = ComponentMapper.getFor(InventoryComponent.class);

    public final Inventory inventory;
}
