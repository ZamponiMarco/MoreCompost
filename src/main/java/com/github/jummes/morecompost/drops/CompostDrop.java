package com.github.jummes.morecompost.drops;

import org.bukkit.block.Block;

public interface CompostDrop {

	/**
	 * Drops the loot represented by the object in the given block
	 * 
	 * @param block to drop loot by
	 */
	public void dropLoot(Block block);
	
	/**
	 * Puts the loot represented by the object in the given container
	 * 
	 * @param block to fill
	 */
	public void putInContainer(Block block);

}
