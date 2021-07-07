package com.github.jummes.morecompost.listener;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.manager.CompostersManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ComposterBreakListener implements Listener {

    @EventHandler
    public void onComposterBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (b.getType().equals(Material.COMPOSTER)) {
            CompostersManager compostersManager = MoreCompost.getInstance().getCompostersManager();
            compostersManager.removeBlockFromPlayer(p.getUniqueId(), b);
        }
    }

}
