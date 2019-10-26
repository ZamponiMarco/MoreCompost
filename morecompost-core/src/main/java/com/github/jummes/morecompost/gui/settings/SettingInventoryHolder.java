package com.github.jummes.morecompost.gui.settings;

import java.util.function.Consumer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.managers.DataManager;

public abstract class SettingInventoryHolder extends MoreCompostInventoryHolder {

	protected DataManager dataManager;
	protected ConfigurationSection section;
	protected String key;
	protected Object value;
	protected HumanEntity player;
	protected InventoryHolder holder;

	public SettingInventoryHolder(DataManager dataManager, ConfigurationSection section, String key, Object value,
			HumanEntity player, InventoryHolder holder) {
		this.dataManager = dataManager;
		this.section = section;
		this.key = key;
		this.value = value;
		this.player = player;
		this.holder = holder;
	}

	protected Consumer<InventoryClickEvent> getBackConsumer() {
		return e -> player.openInventory(holder.getInventory());
	}

}
