package dev.game.test.api.inventory;

public interface IInventory extends Iterable<IItemStack> {

    int getSize();

    String getName();

    IItemStack[] getContents();

    IItemStack getItemStack(int index);

    void setItemStack(int index, IItemStack stack);
}
