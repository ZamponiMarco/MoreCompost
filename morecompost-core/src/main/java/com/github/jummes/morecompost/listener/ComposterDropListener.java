package com.github.jummes.morecompost.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.manager.CompostablesManager;
import com.github.jummes.morecompost.manager.DropsManager;

public class ComposterDropListener implements Listener {

	private static final String METADATA_KEY = "forcedDropTableId";

	@EventHandler
	public void onComposterInteract(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.COMPOSTER)
				&& !e.getPlayer().isSneaking() && e.getHand().equals(EquipmentSlot.HAND)) {
			Levelled composter = (Levelled) block.getBlockData();
			Player player = e.getPlayer();
			if (composter.getLevel() == composter.getMaximumLevel()) {
				dropFromComposter(player, block);
			} else if (composter.getLevel() < composter.getMaximumLevel() - 1) {
				compostBlock(player, block);
			}
			e.setCancelled(true);
		}
	}

	private void compostBlock(Player p, Block composter) {
		CompostablesManager compostablesManager = MoreCompost.getInstance().getCompostablesManager();
		if (compostablesManager.getHighestPriorityCompostableTable(p).compost(composter,
				p.getInventory().getItemInMainHand())) {
			int amount = p.getInventory().getItemInMainHand().getAmount();
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				p.getInventory().getItemInMainHand().setAmount(--amount);
			}
			composter.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, composter.getLocation().clone().add(.5, .6, .5),
					10, .25, .2, .25);
		}
	}

	private void dropFromComposter(Player player, Block composter) {
		DropsManager dropsManager = MoreCompost.getInstance().getDropsManager();
		Levelled levelled = (Levelled) composter.getBlockData();
		if (composter.hasMetadata(METADATA_KEY)) {
			dropsManager.getDropTableById(composter.getMetadata(METADATA_KEY).get(0).asString()).dropAllLoot(composter);
			composter.removeMetadata(METADATA_KEY, MoreCompost.getInstance());
		} else {
			dropsManager.getHighestPriorityDropTable(player).dropAllLoot(composter);
		}
		levelled.setLevel(0);
		composter.setBlockData(levelled);
	}

}
