package com.github.jummes.morecompost.wrapper;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface VersionWrapper {

	public void dropKeyedItem(Location location, ItemStack item);
	
	public ItemStack skullFromValue(String value);
	
}
