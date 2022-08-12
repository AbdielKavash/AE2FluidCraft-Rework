package com.glodblock.github.nei.recipes;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.glodblock.github.client.gui.container.FluidPrioritization;
import com.glodblock.github.nei.object.IRecipeExtractor;
import com.glodblock.github.nei.object.IRecipeExtractorLegacy;
import com.glodblock.github.nei.object.OrderStack;
import ibxm.Player;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.ForgeCommand;
import tv.twitch.chat.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import net.minecraft.util.ChatComponentText;

public final class FluidRecipe {

    private static final HashMap<String, IRecipeExtractor> IdentifierMap = new HashMap<>();
    private static final HashMap<String, IRecipeExtractor> IdentifierMapLegacy = new HashMap<>();

    public static void addRecipeMap(String recipeIdentifier, IRecipeExtractor extractor) {
        IdentifierMap.put(recipeIdentifier, extractor);
        if (recipeIdentifier == null && extractor instanceof IRecipeExtractorLegacy) {
            IdentifierMapLegacy.put(((IRecipeExtractorLegacy) extractor).getClassName(), extractor);
        }
    }

    public static List<OrderStack<?>> getPackageInputs(IRecipeHandler recipe, int index, boolean priority) {
        TemplateRecipeHandler tRecipe = (TemplateRecipeHandler) recipe;
        if (tRecipe == null || !IdentifierMap.containsKey(tRecipe.getOverlayIdentifier())) return new ArrayList<>();
        if (tRecipe.getOverlayIdentifier() == null) return getPackageInputsLegacy(recipe, index);
        IRecipeExtractor extractor = IdentifierMap.get(tRecipe.getOverlayIdentifier());
        if (extractor == null) return new ArrayList<>();
        List<PositionedStack> tmp = new ArrayList<>(tRecipe.getIngredientStacks(index));
        if (priority) {
                for (int i = 0; i < tmp.size(); i++) {
                    if(tmp.get(i).toString().contains("GregTech_FluidDisplay")) {
                        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(String.valueOf(tmp.size())));
                        PositionedStack Stack = tmp.get(i);
                        tmp.remove(Stack);
                        tmp.add(0, Stack);
                        }
                    }
                }
        return extractor.getInputIngredients(tmp, recipe, index);
    }

    public static List<OrderStack<?>> getPackageOutputs(IRecipeHandler recipe, int index, boolean useOther) {
        TemplateRecipeHandler tRecipe = (TemplateRecipeHandler) recipe;
        if (tRecipe == null || !IdentifierMap.containsKey(tRecipe.getOverlayIdentifier())) return new ArrayList<>();
        if (tRecipe.getOverlayIdentifier() == null) return getPackageOutputsLegacy(recipe, index, useOther);
        IRecipeExtractor extractor = IdentifierMap.get(tRecipe.getOverlayIdentifier());
        if (extractor == null) return new ArrayList<>();
        List<PositionedStack> tmp = new ArrayList<>(Collections.singleton(tRecipe.getResultStack(index)));
        if (useOther) tmp.addAll(tRecipe.getOtherStacks(index));
        return extractor.getOutputIngredients(tmp, recipe, index);
    }

    public static List<String> getSupportRecipes() {
        return new ArrayList<>(IdentifierMap.keySet());
    }

    public static List<OrderStack<?>> getPackageInputsLegacy(IRecipeHandler recipe, int index) {
        if (recipe == null || !IdentifierMapLegacy.containsKey(recipe.getClass().getName())) return new ArrayList<>();
        IRecipeExtractor extractor = IdentifierMapLegacy.get(recipe.getClass().getName());
        if (extractor == null) return new ArrayList<>();
        List<PositionedStack> tmp = new ArrayList<>(recipe.getIngredientStacks(index));
        return extractor.getInputIngredients(tmp, recipe, index);
    }

    public static List<OrderStack<?>> getPackageOutputsLegacy(IRecipeHandler recipe, int index, boolean useOther) {
        if (recipe == null || !IdentifierMapLegacy.containsKey(recipe.getClass().getName())) return new ArrayList<>();
        IRecipeExtractor extractor = IdentifierMapLegacy.get(recipe.getClass().getName());
        if (extractor == null) return new ArrayList<>();
        List<PositionedStack> tmp = new ArrayList<>(Collections.singleton(recipe.getResultStack(index)));
        if (useOther) tmp.addAll(recipe.getOtherStacks(index));
        return extractor.getOutputIngredients(tmp, recipe, index);
    }


}
