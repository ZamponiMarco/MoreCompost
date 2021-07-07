package com.github.jummes.morecompost.listener;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.manager.CompostersManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class ComposterPlaceListener implements Listener {

    @EventHandler
    public void onComposterPlace(BlockPlaceEvent e) {

        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (b.getType().equals(Material.COMPOSTER)) {

            b.setMetadata("owner", new FixedMetadataValue(MoreCompost.getInstance(), p.getUniqueId()));
            CompostersManager compostersManager = MoreCompost.getInstance().getCompostersManager();
            compostersManager.addBlockToPlayer(p.getUniqueId(), b);

        }

    }

}
