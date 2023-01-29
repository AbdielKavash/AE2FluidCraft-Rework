package com.glodblock.github.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import appeng.api.storage.ITerminalHost;
import appeng.core.localization.GuiText;

import com.glodblock.github.FluidCraft;
import com.glodblock.github.client.gui.base.FCGuiAmount;
import com.glodblock.github.client.gui.container.ContainerPatternValueAmount;
import com.glodblock.github.common.parts.PartFluidPatternTerminal;
import com.glodblock.github.common.parts.PartFluidPatternTerminalEx;
import com.glodblock.github.inventory.gui.GuiType;
import com.glodblock.github.loader.ItemAndBlockHolder;
import com.glodblock.github.network.CPacketPatternValueSet;

public class GuiPatternValueAmount extends FCGuiAmount {

    public GuiPatternValueAmount(final InventoryPlayer inventoryPlayer, final ITerminalHost te) {
        super(new ContainerPatternValueAmount(inventoryPlayer, te));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.submit.displayString = GuiText.Set.getLocal();
    }

    @Override
    public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(GuiText.SelectAmount.getLocal(), 8, 6, 0x404040);
    }

    @Override
    public void drawBG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        super.drawBG(offsetX, offsetY, mouseX, mouseY);
        try {
            int result = getAmount();
            this.submit.enabled = result > 0;
        } catch (final NumberFormatException e) {
            this.submit.enabled = false;
        }
        this.amountBox.drawTextBox();
    }

    @Override
    protected void actionPerformed(final GuiButton btn) {
        super.actionPerformed(btn);
        try {
            if (btn == this.submit && btn.enabled) {
                FluidCraft.proxy.netHandler.sendToServer(
                        new CPacketPatternValueSet(
                                originalGui.ordinal(),
                                getAmount(),
                                ((ContainerPatternValueAmount) this.inventorySlots).getValueIndex()));
            }
        } catch (final NumberFormatException e) {
            this.amountBox.setText("1");
        }
    }

    @Override
    protected void setOriginGUI(Object target) {
        if (target instanceof PartFluidPatternTerminal) {
            this.myIcon = new ItemStack(ItemAndBlockHolder.FLUID_TERMINAL, 1);
            this.originalGui = GuiType.FLUID_PATTERN_TERMINAL;
        }
        if (target instanceof PartFluidPatternTerminalEx) {
            this.myIcon = new ItemStack(ItemAndBlockHolder.FLUID_TERMINAL_EX, 1);
            this.originalGui = GuiType.FLUID_PATTERN_TERMINAL_EX;
        }
    }

    @Override
    protected String getBackground() {
        return "guis/craftAmt.png";
    }

    public void setAmount(int amount) {
        this.amountBox.setText(String.valueOf(amount));
        this.amountBox.setSelectionPos(0);
    }
}
