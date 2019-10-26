package com.github.jummes.morecompost.gui.droptables;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.collect.Lists;

public class DropTablesListInventoryHolder extends MoreCompostInventoryHolder {

	private static final int DROP_TABLES_NUMBER = 50;
	private static final String DROP_TABLES_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19";
	private static final String ERROR_NOT_IN_CONFIG_MSG = MessageUtils
			.color("&c&lThis drop table is not present in config files. &6&lCreate it &l&cfirst.");

	private String title;
	private int page;

	public DropTablesListInventoryHolder(String title, int page) {
		this.title = title;
		this.page = page;
	}

	@Override
	protected void initializeInventory() {
		DropsManager manager = MoreCompost.getInstance().getDropsManager();
		ConfigurationSection section = manager.getDataYaml().getRoot();

		List<DropTable> dropTables = Lists.newArrayList(manager.getPercentages().values());
		List<DropTable> toList = dropTables.stream()
				.filter(dropTable -> dropTables.indexOf(dropTable) >= (page - 1) * DROP_TABLES_NUMBER
						&& dropTables.indexOf(dropTable) <= page * DROP_TABLES_NUMBER - 1)
				.collect(Collectors.toList());

		int maxPage = (int) Math.ceil((dropTables.size() > 0 ? dropTables.size() : 1) / (double) DROP_TABLES_NUMBER);

		// Create inventory
		this.inventory = Bukkit.createInventory(this, 54, title);

		// List drop tables
		toList.forEach(dropTable -> registerClickConsumer(toList.indexOf(dropTable), getDropItem(dropTable), (e) -> {
			if (dropTable.isPresentInConfig())
				e.getWhoClicked()
						.openInventory(new DropTableSettingsInventoryHolder(this, dropTable.getId()).getInventory());
			else
				e.getWhoClicked().sendMessage(ERROR_NOT_IN_CONFIG_MSG);
		}));

		// Register add item
		registerClickConsumer(51, getAddItem(),
				e -> e.getWhoClicked().openInventory(
						new StringSettingInventoryHolder(manager, section, null, null, e.getWhoClicked(), this)
								.getInventory()));

		// Register page swap items
		if (page != maxPage) {
			registerClickConsumer(53, getNextPageItem(), e -> e.getWhoClicked()
					.openInventory(new DropTablesListInventoryHolder(title, page + 1).getInventory()));
		}
		if (page != 1) {
			registerClickConsumer(52, getPreviousPageItem(), e -> e.getWhoClicked()
					.openInventory(new DropTablesListInventoryHolder(title, page - 1).getInventory()));
		}

		// Fill remaining inventory
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private ItemStack getDropItem(DropTable dropTable) {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(DROP_TABLES_HEAD),
				MessageUtils.color("&6&lDropTable: &l&2" + dropTable.getId()));
	}

}
