package com.github.jummes.morecompost.drops;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractCompostDrop {

	protected String id;
	protected int weight;

	/**
	 * Drops the loot represented by the object in the given block
	 * 
	 * @param block to drop loot by
	 */
	public abstract void dropLoot(Block block);

	/**
	 * Puts the loot represented by the object in the given container
	 * 
	 * @param block to fill
	 */
	public abstract void putInContainer(Block block);

	public abstract ItemStack getGUIItem();

	public abstract String getType();

}
