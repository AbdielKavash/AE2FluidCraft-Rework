package com.glodblock.github.inventory.gui;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class PartGuiFactory<T> extends TileGuiFactory<T> {

    PartGuiFactory(Class<T> invClass) {
        super(invClass);
    }

    @Nullable
    @Override
    protected T getInventory(TileEntity tile, ForgeDirection face) {
        if (tile instanceof IPartHost) {
            IPart part = ((IPartHost) tile).getPart(face);
            if (invClass.isInstance(part)) {
                return invClass.cast(part);
            }
        }
        return null;
    }
}
