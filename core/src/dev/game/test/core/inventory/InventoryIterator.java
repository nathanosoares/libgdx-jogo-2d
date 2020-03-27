package dev.game.test.core.inventory;

import dev.game.test.api.inventory.IInventory;
import dev.game.test.api.inventory.IItemStack;
import lombok.RequiredArgsConstructor;

import java.util.ListIterator;

@RequiredArgsConstructor
public class InventoryIterator implements ListIterator<IItemStack> {

    private final IInventory inventory;

    private int nextIndex = 0;

    /**
     * 0 = haven't moved yet
     * 1 = forward
     * -1 = backward
     */
    private byte direction;

    // NEXT
    @Override
    public boolean hasNext() {
        return this.nextIndex < this.inventory.getSize();
    }

    @Override
    public IItemStack next() {
        this.direction = 1;
        return this.inventory.getItemStack(this.nextIndex++);
    }

    @Override
    public int nextIndex() {
        return this.nextIndex;
    }
    // END NEXT

    // PREVIOUS
    @Override
    public boolean hasPrevious() {
        return this.nextIndex > 0;
    }

    @Override
    public IItemStack previous() {
        this.direction = -1;
        return this.inventory.getItemStack(--this.nextIndex);
    }

    @Override
    public int previousIndex() {
        return this.nextIndex - 1;
    }
    // END PREVIOUS

    // MODIFIERS
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }

    @Override
    public void set(IItemStack stack) {
        if (this.direction == 0) {
            throw new IllegalStateException("Unknown direction!");
        }

        int index = this.direction == 1 ? nextIndex - 1 : nextIndex;

        this.inventory.setItemStack(index, stack);
    }

    @Override
    public void add(IItemStack iItemStack) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
    // END MODIFIERS
}
