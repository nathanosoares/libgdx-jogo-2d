package dev.game.test.core.inventory;

import dev.game.test.api.inventory.IInventory;
import dev.game.test.api.inventory.IItemStack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

@Getter
@RequiredArgsConstructor
public class Inventory implements IInventory {

    private final int size;
    private final String name;
    private final IItemStack[] contents;

    @Override
    public final Iterator<IItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public IItemStack getItemStack(int index) {
        return this.contents[index];
    }

    @Override
    public void setItemStack(int index, IItemStack stack) {
        this.contents[index] = stack;
    }
}
