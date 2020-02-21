package com.github.jummes.morecompost.dropdescription;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.libs.util.ItemUtils;

@SerializableAs("NoDropDescription")
public class NoDropDescription extends DropDescription {

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

}
