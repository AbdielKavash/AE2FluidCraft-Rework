package com.glodblock.github.inventory.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemInventory {

    Object getInventory(ItemStack stack, World world, int x, int y, int z);
}
