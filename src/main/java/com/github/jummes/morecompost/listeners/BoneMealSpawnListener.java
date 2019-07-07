package com.github.jummes.morecompost.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class BoneMealSpawnListener implements Listener {

	private static final String CUSTOM_KEY = "sasso";

	@EventHandler
	public void onBoneMealSpawn(ItemSpawnEvent e) {

		Item item = e.getEntity();

		if (!item.hasMetadata(CUSTOM_KEY) && isSpawnedByComposter(item)) {
			e.setCancelled(true);
		}

	}

	private boolean isSpawnedByComposter(Item item) {
		return item.getItemStack().getType().equals(Material.BONE_MEAL)
				&& (item.getLocation().getBlock().getType().equals(Material.COMPOSTER) || item.getLocation().clone()
						.subtract(0, 1, 0).getBlock().getType().equals(Material.COMPOSTER));
	}

}
