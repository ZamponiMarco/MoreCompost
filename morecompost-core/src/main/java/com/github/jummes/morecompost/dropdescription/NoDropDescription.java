package com.github.jummes.morecompost.dropdescription;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lNone", description = "gui.droptable.no-drop-description.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==")
@SerializableAs("NoDropDescription")
public class NoDropDescription extends DropDescription {

    public static NoDropDescription deserialize(@SuppressWarnings("unused") Map<String, Object> map) {
        return new NoDropDescription();
    }

    @Override
    public void dropLoot(Block block) {
    }

    @Override
    public void putInContainer(Block block) {
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.BARRIER), "&c&lNothing", new ArrayList<String>());
    }

    @Override
    public String toString() {
        return "Nothing";
    }

}
