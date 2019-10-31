package com.github.jummes.morecompost.gui.droptables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.drops.DropsListInventoryHolder;
import com.github.jummes.morecompost.gui.settings.IntegerSettingInventoryHolder;
import com.github.jummes.morecompost.locales.LocaleString;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.collect.Lists;

public class DropTableSettingsInventoryHolder extends MoreCompostInventoryHolder {

	private static final String PRIORITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZjYjY2ZDg2NmY1YmVhYTAyMjRhZjFhNDEyMDYwOTllNmEzZjdmYzVjNWYyMTY2NjEzOTg1OTUyOGFiNSJ9fX0=";
	private static final String MINIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MmM5ZDYyOGJiMzIyMWVmMzZiNGNiZDBiOWYxNWVkZDU4ZTU4NjgxODUxNGQ3ZTgyM2Q1NWM0OGMifX19=";
	private static final String MAXIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUxNDlkZGRhZGVkMjBkMjQ0ZTBiYjYyYTJkOWZhMGRjNmM2YTc4NjI1NTkzMjhhOTRmNzc3MjVmNTNjMzU4In19fQ===";
	private static final String DROPS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODllNzAxNjIxNDNjN2NhYTIwZTMwM2VlYTMxNGE5YWVkNWRiOWNjNjg0MzVlNzgzYjNjNTlhZjQzYmY0MzYzNSJ9fX0====";

	private InventoryHolder holder;
	private String dropTableId;

	public DropTableSettingsInventoryHolder(InventoryHolder holder, String dropTableId) {
		this.holder = holder;
		this.dropTableId = dropTableId;
	}

	@Override
	protected void initializeInventory() {
		DropsManager manager = MoreCompost.getInstance().getDropsManager();

		DropTable dropTable = manager.getDropTableById(dropTableId);
		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(dropTableId);

		this.inventory = Bukkit.createInventory(this, 27,
				MessageUtils.color(String.format("&6&lDropTable: &1&l%s", dropTable.getId())));
		registerSettingConsumer(3, manager, section, wrapper.skullFromValue(MINIMUM_HEAD), "minRolls",
				dropTable.getMinRolls(), localesManager.getLocaleString(LocaleString.MIN_ROLLS_DESCRIPTION),
				IntegerSettingInventoryHolder.class, true);
		registerSettingConsumer(5, manager, section, wrapper.skullFromValue(MAXIMUM_HEAD), "maxRolls",
				dropTable.getMaxRolls(), localesManager.getLocaleString(LocaleString.MAX_ROLLS_DESCRIPTION),
				IntegerSettingInventoryHolder.class, true);

		if (!dropTableId.equals("default")) {
			registerSettingConsumer(4, manager, section, wrapper.skullFromValue(PRIORITY_HEAD), "priority",
					dropTable.getPriority(), localesManager.getLocaleString(LocaleString.PRIORITY_DESCRIPTION),
					IntegerSettingInventoryHolder.class, false);
		}
		registerClickConsumer(13, getNamedItem(wrapper.skullFromValue(DROPS_HEAD), "&6&lDrops",
				localesManager.getLocaleString(LocaleString.DROPS_LIST_DESCRIPTION)), e -> {
					e.getWhoClicked()
							.openInventory(new DropsListInventoryHolder(this, MessageUtils.color("&6&lDrops"),
									dropTable.getId(), Lists.newArrayList(dropTable.getWeightMap().values()), 1)
											.getInventory());
				});
		registerClickConsumer(18, getRemoveItem(), e -> {
			section.getParent().set(dropTableId, null);
			manager.saveAndReloadData();
			e.getWhoClicked().openInventory(holder.getInventory());
		});
		registerClickConsumer(26, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);

	}

}
