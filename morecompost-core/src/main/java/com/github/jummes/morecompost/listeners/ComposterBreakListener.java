package com.github.jummes.morecompost.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.managers.CompostersManager;

public class ComposterBreakListener implements Listener {

	@EventHandler
	public void onComposterBreak(BlockBreakEvent e) {

		Block b = e.getBlock();

		if (b.getType().equals(Material.COMPOSTER)) {
			CompostersManager compostersManager = MoreCompost.getInstance().getCompostersManager();
			compostersManager.removeBlockFromPlayer(b);	
		}
	}

}
