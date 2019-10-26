package com.github.jummes.morecompost.listeners;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.drops.DropSettingsInventoryHolder;
import com.github.jummes.morecompost.gui.drops.factory.DropSettingsInventoryHolderFactory;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.managers.DataManager;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.utils.MessageUtils;

public class PlayerChatListener implements Listener {

	private MoreCompost plugin;

	public PlayerChatListener() {
		this.plugin = MoreCompost.getInstance();
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Map<HumanEntity, Entry<DataManager, Entry<ConfigurationSection, Entry<String, InventoryHolder>>>> settingsMap = StringSettingInventoryHolder
				.getSettingsMap();
		Player p = e.getPlayer();
		if (settingsMap != null && settingsMap.get(p) != null) {
			String key = settingsMap.get(p).getValue().getValue().getKey();
			if (key != null) {
				runModifySyncTask(p, e.getMessage(), settingsMap);
			} else {
				runCreateSyncTask(p, e.getMessage(), settingsMap);
			}
			e.setCancelled(true);
		}
	}

	private void runCreateSyncTask(Player p, String value,
			Map<HumanEntity, Entry<DataManager, Entry<ConfigurationSection, Entry<String, InventoryHolder>>>> settingsMap) {
		plugin.getServer().getScheduler().runTask(plugin, () -> {
			if (!value.equalsIgnoreCase("exit")) {
				DataManager manager = settingsMap.get(p).getKey();
				ConfigurationSection section = settingsMap.get(p).getValue().getKey();

				if (manager instanceof DropsManager) {
					if (section.getName().equalsIgnoreCase("drops")) {
						String dropTableId = section.getParent().getName();
						((DropsManager) manager).getDefaultDrop(dropTableId, value);
					} else {
						((DropsManager) manager).getDefaultDropTable(value);
					}
				} else if (manager instanceof CompostablesManager) {
					if (section.getName().equalsIgnoreCase("compostables")) {
						String compostableTableId = section.getParent().getName();
						((CompostablesManager) manager).getDefaultCompostable(compostableTableId, value);
					} else {
						((CompostablesManager) manager).getDefaultCompostableTable(value);
					}
				}
				manager.reloadData();
				p.sendMessage(MessageUtils.color("&a&lObject created."));
				p.openInventory(settingsMap.get(p).getValue().getValue().getValue().getInventory());
			} else {
				p.sendMessage(MessageUtils.color("&aThe value &6&lhasn't&a been modified."));
			}
			settingsMap.remove(p);
		});
	}

	private void runModifySyncTask(Player p, String value,
			Map<HumanEntity, Entry<DataManager, Entry<ConfigurationSection, Entry<String, InventoryHolder>>>> settingsMap) {
		plugin.getServer().getScheduler().runTask(plugin, () -> {
			String validatedValue = value.trim();
			if (!validatedValue.equalsIgnoreCase("exit")) {
				// TODO test
				DataManager manager = settingsMap.get(p).getKey();
				ConfigurationSection section = settingsMap.get(p).getValue().getKey();
				String key = settingsMap.get(p).getValue().getValue().getKey();
				section.set(key, validatedValue);
				manager.reloadData();
				p.sendMessage(MessageUtils.color("&aObject modified, &6" + key + ": &e" + validatedValue));

				InventoryHolder precHolder = settingsMap.get(p).getValue().getValue().getValue();

				if (key.equalsIgnoreCase("type")) {
					DropSettingsInventoryHolder dropHolder = (DropSettingsInventoryHolder) precHolder;
					DropsManager dropsManager = (DropsManager) manager;
					p.openInventory(DropSettingsInventoryHolderFactory
							.buildDropSettingInventoryHolder(dropHolder.getHolder(),
									dropsManager.get(dropHolder.getDropTableId()),
									dropsManager.get(dropHolder.getDropTableId()).get(dropHolder.getDropId()))
							.getInventory());
				} else {
					p.openInventory(precHolder.getInventory());
				}
			} else {
				p.sendMessage(MessageUtils.color("&aThe value &6&lhasn't&a been modified."));
			}
			settingsMap.remove(p);
		});
	}

}
