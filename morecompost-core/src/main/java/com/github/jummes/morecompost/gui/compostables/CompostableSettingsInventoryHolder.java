package com.github.jummes.morecompost.gui.compostables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.compostables.Compostable;
import com.github.jummes.morecompost.compostabletables.CompostableTable;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.settings.DoubleSettingInventoryHolder;
import com.github.jummes.morecompost.gui.settings.IntegerSettingInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.locales.LocaleString;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.utils.MessageUtils;

public class CompostableSettingsInventoryHolder extends MoreCompostInventoryHolder {

	private static final String CHANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIxNmQxN2RlNDJiZDA5NzY2OWI4ZTA5ZThlNjJkZjhiZjc4MzdkMzk1OTc1NDk2ZTYzNmZkYTRmYTk1ZjNkIn19fQ==";
	private static final String MINIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MmM5ZDYyOGJiMzIyMWVmMzZiNGNiZDBiOWYxNWVkZDU4ZTU4NjgxODUxNGQ3ZTgyM2Q1NWM0OGMifX19=";
	private static final String MAXIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUxNDlkZGRhZGVkMjBkMjQ0ZTBiYjYyYTJkOWZhMGRjNmM2YTc4NjI1NTkzMjhhOTRmNzc3MjVmNTNjMzU4In19fQ===";
	private static final String MATERIAL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTI5ZWE2OGEwYzYxYjFlZGEyZDhhZWMzZTIyMjk3MjczMjNiN2QyZGE2YmMwNGNjMGNkMmRlZjNiNDcxMiJ9fX0====";
	private static final String FORCED_DROPTABLE_ID = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc0ZDEzYjUxMDE2OGM3YWNiNDRiNjQ0MTY4NmFkN2FiMWNiNWI3NDg4ZThjZGY5ZDViMjJiNDdjNDgzZjIzIn19fQ======";

	private InventoryHolder holder;
	private String compostableTableId;
	private String compostableId;

	public CompostableSettingsInventoryHolder(InventoryHolder holder, String compostableTableId, String compostableId) {
		this.holder = holder;
		this.compostableTableId = compostableTableId;
		this.compostableId = compostableId;
	}

	@Override
	protected void initializeInventory() {
		CompostablesManager manager = MoreCompost.getInstance().getCompostablesManager();

		CompostableTable compostableTable = manager.get(compostableTableId);
		Compostable compostable = compostableTable.get(compostableId);

		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(compostableTable.getId())
				.getConfigurationSection("compostables").getConfigurationSection(compostable.getId());

		this.inventory = Bukkit.createInventory(this, 27,
				MessageUtils.color(String.format("&6&lCompostable: &1&l%s", compostable.getId())));
		registerSettingConsumer(4, manager, section, wrapper.skullFromValue(CHANCE_HEAD), "chance",
				compostable.getChance(), localesManager.getLocaleString(LocaleString.CHANCE_DESCRIPTION),
				DoubleSettingInventoryHolder.class);
		registerSettingConsumer(3, manager, section, wrapper.skullFromValue(MINIMUM_HEAD), "minRolls",
				compostable.getMinRolls(), localesManager.getLocaleString(LocaleString.MIN_ROLLS_DESCRIPTION),
				IntegerSettingInventoryHolder.class);
		registerSettingConsumer(5, manager, section, wrapper.skullFromValue(MAXIMUM_HEAD), "maxRolls",
				compostable.getMaxRolls(), localesManager.getLocaleString(LocaleString.MAX_ROLLS_DESCRIPTION),
				IntegerSettingInventoryHolder.class);
		registerSettingConsumer(13, manager, section, wrapper.skullFromValue(MATERIAL_HEAD), "material",
				compostable.getMaterial().name(), localesManager.getLocaleString(LocaleString.MATERIAL_DESCRIPTION),
				StringSettingInventoryHolder.class);
		registerSettingConsumer(17, manager, section, wrapper.skullFromValue(FORCED_DROPTABLE_ID), "forcedDropTableId",
				compostable.getForcedDropTableId().orElse("null"),
				localesManager.getLocaleString(LocaleString.FORCED_DROP_TABLE_ID_DESCRIPTION),
				StringSettingInventoryHolder.class);
		registerClickConsumer(18, getRemoveItem(), e -> {
			section.getParent().set(compostableId, null);
			manager.saveAndReloadData();
			e.getWhoClicked().openInventory(holder.getInventory());
		});
		registerClickConsumer(26, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);

	}

}
