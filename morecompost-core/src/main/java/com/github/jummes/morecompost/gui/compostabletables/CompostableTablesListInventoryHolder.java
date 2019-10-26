package com.github.jummes.morecompost.gui.compostabletables;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.compostabletables.CompostableTable;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.collect.Lists;

public class CompostableTablesListInventoryHolder extends MoreCompostInventoryHolder {

	private static final int COMPOSTABLES_TABLES_NUMBER = 50;
	private static final String COMPOSTABLES_TABLES_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTI5ZWE2OGEwYzYxYjFlZGEyZDhhZWMzZTIyMjk3MjczMjNiN2QyZGE2YmMwNGNjMGNkMmRlZjNiNDcxMiJ9fX0=";
	private static final String ERROR_NOT_IN_CONFIG_MSG = MessageUtils
			.color("&c&lThis compostable table is not present in config files. &6&lCreate it &l&cfirst.");

	private String title;
	private int page;

	public CompostableTablesListInventoryHolder(String title, List<CompostableTable> compostableTables, int page) {
		this.title = title;
		this.page = page;
	}

	@Override
	protected void initializeInventory() {
		CompostablesManager manager = MoreCompost.getInstance().getCompostablesManager();
		ConfigurationSection section = manager.getDataYaml().getRoot();

		List<CompostableTable> compostableTables = Lists.newArrayList(manager.getCompostables().values());
		List<CompostableTable> toList = compostableTables.stream()
				.filter(dropTable -> compostableTables.indexOf(dropTable) >= (page - 1) * COMPOSTABLES_TABLES_NUMBER
						&& compostableTables.indexOf(dropTable) <= page * COMPOSTABLES_TABLES_NUMBER - 1)
				.collect(Collectors.toList());

		int maxPage = (int) Math.ceil(
				(compostableTables.size() > 0 ? compostableTables.size() : 1) / (double) COMPOSTABLES_TABLES_NUMBER);

		this.inventory = Bukkit.createInventory(this, 54, title);
		toList.forEach(compostableTable -> registerClickConsumer(toList.indexOf(compostableTable),
				getCompostableTableItem(compostableTable), (e) -> {
					if (compostableTable.isPresentInConfig())
						e.getWhoClicked().openInventory(
								new CompostableTableSettingsInventoryHolder(this, compostableTable.getId())
										.getInventory());
					else
						e.getWhoClicked().sendMessage(ERROR_NOT_IN_CONFIG_MSG);
				}));

		registerClickConsumer(51, getAddItem(),
				e -> e.getWhoClicked().openInventory(
						new StringSettingInventoryHolder(manager, section, null, null, e.getWhoClicked(), this)
								.getInventory()));

		if (page != maxPage) {
			registerClickConsumer(53, getNextPageItem(), e -> e.getWhoClicked().openInventory(
					new CompostableTablesListInventoryHolder(title, compostableTables, page + 1).getInventory()));
		}
		if (page != 1) {
			registerClickConsumer(52, getPreviousPageItem(), e -> e.getWhoClicked().openInventory(
					new CompostableTablesListInventoryHolder(title, compostableTables, page - 1).getInventory()));
		}
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private ItemStack getCompostableTableItem(CompostableTable compostableTable) {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(COMPOSTABLES_TABLES_HEAD),
				MessageUtils.color("&6&lCompostableTable: &l&2" + compostableTable.getId()));
	}

}
