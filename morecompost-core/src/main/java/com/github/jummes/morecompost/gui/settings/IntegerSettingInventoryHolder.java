package com.github.jummes.morecompost.gui.settings;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.managers.DataManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

public class IntegerSettingInventoryHolder extends SettingInventoryHolder {

	private static final String ARROW_LEFT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk5ZjA0OTI4OGFjNTExZjZlN2VjNWM5MjM4Zjc2NTI3YzJmYmNhZDI4NTc0MzZhYzM4MTU5NmNjMDJlNCJ9fX0==";
	private static final String ARROW2_LEFT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZlMTQ1ZTcxMjk1YmNjMDQ4OGU5YmI3ZTZkNjg5NWI3Zjk2OWEzYjViYjdlYjM0YTUyZTkzMmJjODRkZjViIn19fQ===";
	private static final String ARROW3_LEFT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzhhMWYwMzdjOGU1MTc4YmJlNGNiOWE3ZDMzNWYxYjExMGM0NWMxYzQ2NWYxZDczZGNiZThjYWQ2OWQ5ZWNhIn19fQ===";
	private static final String ARROW_RIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY4YWQyNTVmOTJiODM5YjVhOGQxYmJiOWJiNGQxYTVmMzI3NDNiNmNmNTM2NjVkOTllZDczMmFhOGJlNyJ9fX0====";
	private static final String ARROW2_RIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY5N2MyNDg5MmNmYzAzYzcyOGZmYWVhYmYzNGJkZmI5MmQ0NTExNDdiMjZkMjAzZGNhZmE5M2U0MWZmOSJ9fX0====";
	private static final String ARROW3_RIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0NDg2ODYzZjMwZTM4NDMyZGJkMjJlNTQxMjk2NDY0NGVjMjVlYTRmOTkxYTM4YzczNzM3NmU5NjA2NDc5In19fQ=====";
	private static final String ZERO_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmEzMTNhY2E1ZDgzZjk3OWZiODhlNjIwNDNiMzhiNmE1ZTQzZDkyYzk4MzFjZWQ0Njk3MTlmNGMzNzYyMmUxIn19fQ=======";
	private static final String SUBMIT = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWE3NWM4ZTUxYzNkMTA1YmFiNGM3ZGUzM2E3NzA5MzczNjRiNWEwMWMxNWI3ZGI4MmNjM2UxZmU2ZWI5MzM5NiJ9fX0======";

	private int result;

	public IntegerSettingInventoryHolder(DataManager dataManager, ConfigurationSection section, String key,
			Object value, HumanEntity player, InventoryHolder holder) {
		super(dataManager, section, key, value, player, holder);
		result = (int) value;
	}

	@Override
	protected void initializeInventory() {
		VersionWrapper wrapper = MoreCompost.getInstance().getWrapper();

		this.inventory = Bukkit.createInventory(this, 27, MessageUtils.color("&6&lModify &e&l" + key));
		registerClickConsumer(11, getModifyItem(-1, wrapper.skullFromValue(ARROW_LEFT_HEAD)), getConsumer(-1));
		registerClickConsumer(10, getModifyItem(-10, wrapper.skullFromValue(ARROW2_LEFT_HEAD)), getConsumer(-10));
		registerClickConsumer(9, getModifyItem(-100, wrapper.skullFromValue(ARROW3_LEFT_HEAD)), getConsumer(-100));
		registerClickConsumer(15, getModifyItem(+1, wrapper.skullFromValue(ARROW_RIGHT_HEAD)), getConsumer(+1));
		registerClickConsumer(16, getModifyItem(+10, wrapper.skullFromValue(ARROW2_RIGHT_HEAD)), getConsumer(+10));
		registerClickConsumer(17, getModifyItem(+100, wrapper.skullFromValue(ARROW3_RIGHT_HEAD)), getConsumer(+100));
		registerClickConsumer(13, getConfirmItem(), e -> {
			section.set(key, result);
			dataManager.saveAndReloadData();
			player.sendMessage(MessageUtils.color("&aObject modified: &6" + key + ": &e" + String.valueOf(result)));
			player.openInventory(holder.getInventory());
		});
		registerClickConsumer(22, getZeroItem(), e -> {
			result = 0;
			inventory.setItem(13, getConfirmItem());
		});
		registerClickConsumer(26, getBackItem(), getBackConsumer());
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private Consumer<InventoryClickEvent> getConsumer(int addition) {
		return e -> {
			if (e.getClick().equals(ClickType.LEFT)) {
				modifyAndReload(addition);
			}
		};
	}

	private void modifyAndReload(int addition) {
		result += addition;
		inventory.setItem(13, getConfirmItem());
	}

	private ItemStack getModifyItem(double addition, ItemStack item) {
		return getNamedItem(item, MessageUtils.color("&6&lModify -> &e&l" + String.valueOf(addition)), new ArrayList<String>());
	}

	private ItemStack getZeroItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ZERO_HEAD),
				MessageUtils.color("&6Set to &e&l0"), new ArrayList<String>());
	}

	private ItemStack getConfirmItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(SUBMIT),
				MessageUtils.color("&6&lResult = &e&l" + String.valueOf(result)), new ArrayList<String>());
	}

}
