package com.github.jummes.morecompost.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.managers.DropsManager;

public class ComposterDropListener implements Listener {

	@EventHandler
	public void onComposterInteract(PlayerInteractEvent e) {

		Block block = e.getClickedBlock();

		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.COMPOSTER)
				&& !e.getPlayer().isSneaking() && e.getHand().equals(EquipmentSlot.HAND)) {

			Levelled composter = (Levelled) block.getBlockData();
			Player player = e.getPlayer();

			if (composter.getLevel() == composter.getMaximumLevel()) {
				DropsManager dropsManager = MoreCompost.getInstance().getDropsManager();
				dropsManager.getPercentages().get(dropsManager.getHighestPermission(player)).dropAllLoot(block);
			} else {
				CompostablesManager compostablesManager = MoreCompost.getInstance().getCompostablesManager();
				e.setCancelled(true);
				compostablesManager.getCompostables().get(compostablesManager.getHighestPermission(player)).stream()
						.filter(compostable -> compostable.getMaterial()
								.equals(player.getInventory().getItemInMainHand().getType()))
						.findFirst().ifPresent(compostable -> compostable.compost(block));

				int amount = player.getInventory().getItemInMainHand().getAmount();
				if (!player.getGameMode().equals(GameMode.CREATIVE)) {
					player.getInventory().getItemInMainHand().setAmount(--amount);
				}
			}

		}

	}

}
