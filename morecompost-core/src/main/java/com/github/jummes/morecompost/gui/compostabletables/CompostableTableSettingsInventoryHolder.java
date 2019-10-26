package com.github.jummes.morecompost.gui.compostabletables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.compostabletables.CompostableTable;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.compostables.CompostablesListInventoryHolder;
import com.github.jummes.morecompost.gui.settings.BooleanSettingInventoryHolder;
import com.github.jummes.morecompost.gui.settings.IntegerSettingInventoryHolder;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

public class CompostableTableSettingsInventoryHolder extends MoreCompostInventoryHolder {

	private static final String PRIORITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZjYjY2ZDg2NmY1YmVhYTAyMjRhZjFhNDEyMDYwOTllNmEzZjdmYzVjNWYyMTY2NjEzOTg1OTUyOGFiNSJ9fX0=";
	private static final String MINIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MmM5ZDYyOGJiMzIyMWVmMzZiNGNiZDBiOWYxNWVkZDU4ZTU4NjgxODUxNGQ3ZTgyM2Q1NWM0OGMifX19=";
	private static final String COMPOSTABLES_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkyMDNlYzgyNTU1NGEwMmQ4NTAxZTMzNThhMGFhZjg5N2NiNTc5MGRjYjFjZjdiMTkzNGI1MWUyZDQ2YjNlNiJ9fX0==";

	private InventoryHolder holder;
	private String compostableTableId;

	public CompostableTableSettingsInventoryHolder(InventoryHolder holder, String compostableTableId) {
		this.holder = holder;
		this.compostableTableId = compostableTableId;
	}

	@Override
	protected void initializeInventory() {
		VersionWrapper wrapper = MoreCompost.getInstance().getWrapper();
		
		CompostablesManager manager = MoreCompost.getInstance().getCompostablesManager();

		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(compostableTableId);

		CompostableTable compostableTable = manager.get(compostableTableId);

		this.inventory = Bukkit.createInventory(this, 27,
				MessageUtils.color(String.format("&6&lCompostableTable: &1&l%s", compostableTable.getId())));
		registerClickConsumer(3,
				getSettingItem(wrapper.skullFromValue(MINIMUM_HEAD), "getReplaceDefaultCompostables",
						compostableTable.getReplaceDefaultCompostables()),
				getSettingConsumer(manager, manager.getDataYaml().getConfigurationSection(compostableTable.getId()),
						"replaceDefaultCompostables", compostableTable.getReplaceDefaultCompostables(),
						BooleanSettingInventoryHolder.class));
		if (!compostableTableId.equals("default")) {
			registerClickConsumer(5,
					getSettingItem(wrapper.skullFromValue(PRIORITY_HEAD), "priority", compostableTable.getPriority()),
					getSettingConsumer(manager, manager.getDataYaml().getConfigurationSection(compostableTable.getId()),
							"priority", compostableTable.getPriority(), IntegerSettingInventoryHolder.class));
		}
		registerClickConsumer(13, getSettingItem(wrapper.skullFromValue(COMPOSTABLES_HEAD), "Compostables", "List"),
				e -> e.getWhoClicked().openInventory(new CompostablesListInventoryHolder(this,
						MessageUtils.color("&2&lCompostables"), compostableTable.getId(), 1).getInventory()));
		registerClickConsumer(18, getRemoveItem(), e -> {
			section.getParent().set(compostableTableId, null);
			manager.reloadData();
			e.getWhoClicked().openInventory(holder.getInventory());
		});
		registerClickConsumer(26, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);

	}

}
