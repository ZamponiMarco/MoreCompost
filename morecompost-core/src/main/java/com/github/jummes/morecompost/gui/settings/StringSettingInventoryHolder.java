package com.github.jummes.morecompost.gui.settings;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.managers.DataManager;
import com.github.jummes.morecompost.utils.MessageUtils;

public class StringSettingInventoryHolder extends SettingInventoryHolder {

	private static final String MODIFY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE3NWM4ZTUxYzNkMTA1YmFiNGM3ZGUzM2E3NzA5MzczNjRiNWEwMWMxNWI3ZGI4MmNjM2UxZmU2ZWI5MzM5NiJ9fX0==";

	private static final String MODIFY_TITLE = MessageUtils.color("&6&lModify &e&l%s");
	private static final String MODIFY_MESSAGE = MessageUtils.color(
			"&aTo modify the parameter type in chat the &6&lnew value&a.\n&aType &6&l'exit' &ato leave the value unmodified.");
	private static final String MODIFY_ITEM_NAME = MessageUtils.color("&6&lModify Value");

	private static final String CREATE_TITLE = MessageUtils.color("&6&lCreate new object");
	private static final String CREATE_MESSAGE = MessageUtils
			.color("&aTo create the object type in chat his &6&lname&a.\n&aType &6&l'exit' &ato cancel the creation.");
	private static final String CREATE_ITEM_NAME = MessageUtils.color("&6&lCreate Object");

	private static Map<HumanEntity, Entry<DataManager, Entry<ConfigurationSection, Entry<String, InventoryHolder>>>> settingsMap = new HashMap<>();

	private boolean isCreation;

	public StringSettingInventoryHolder(DataManager dataManager, ConfigurationSection section, String key, Object value,
			HumanEntity player, InventoryHolder holder) {
		super(dataManager, section, key, value, player, holder);
		isCreation = key == null;
	}

	@Override
	protected void initializeInventory() {
		this.inventory = Bukkit.createInventory(this, 27, isCreation ? CREATE_TITLE : String.format(MODIFY_TITLE, key));
		registerClickConsumer(13, getStringItem(MoreCompost.getInstance().getWrapper().skullFromValue(MODIFY_HEAD)), e -> playerCanWrite());
		registerClickConsumer(26, getBackItem(), getBackConsumer());
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private void playerCanWrite() {
		player.sendMessage(isCreation ? CREATE_MESSAGE : MODIFY_MESSAGE);
		settingsMap.put(player, new AbstractMap.SimpleEntry<>(dataManager,
				new AbstractMap.SimpleEntry<>(section, new AbstractMap.SimpleEntry<>(key, holder))));
		player.closeInventory();
	}

	public static Map<HumanEntity, Entry<DataManager, Entry<ConfigurationSection, Entry<String, InventoryHolder>>>> getSettingsMap() {
		return settingsMap;
	}

	private ItemStack getStringItem(ItemStack item) {
		return getNamedItem(item, isCreation ? CREATE_ITEM_NAME : MODIFY_ITEM_NAME);
	}
}
