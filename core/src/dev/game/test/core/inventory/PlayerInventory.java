package dev.game.test.core.inventory;

import dev.game.test.api.inventory.IItemStack;
import dev.game.test.core.entity.Player;
import lombok.Getter;

@Getter
public class PlayerInventory extends Inventory {

    private final Player player;

    public PlayerInventory(Player player) {
        super(9, "Inventory of " + player.getName(), new IItemStack[9]);
        this.player = player;
    }
}
