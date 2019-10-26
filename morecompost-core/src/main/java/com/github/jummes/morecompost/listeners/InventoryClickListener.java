package com.github.jummes.morecompost.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
			return;
		}

		if (e.getClickedInventory().getHolder() instanceof MoreCompostInventoryHolder) {
			((MoreCompostInventoryHolder) e.getClickedInventory().getHolder()).handleClickEvent(e);
		}
	}
	
}
