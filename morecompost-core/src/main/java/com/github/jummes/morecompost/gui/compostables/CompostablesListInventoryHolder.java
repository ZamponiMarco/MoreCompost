package com.github.jummes.morecompost.gui.compostables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.compostables.Compostable;
import com.github.jummes.morecompost.compostabletables.CompostableTable;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.utils.MessageUtils;

public class CompostablesListInventoryHolder extends MoreCompostInventoryHolder {

	private static final int COMPOSTABLES_NUMBER = 50;

	private InventoryHolder holder;
	private String title;
	private String compostableTableId;
	private int page;

	public CompostablesListInventoryHolder(InventoryHolder holder, String title, String compostableTableId, int page) {
		this.holder = holder;
		this.title = title;
		this.compostableTableId = compostableTableId;
		this.page = page;
	}

	@Override
	protected void initializeInventory() {
		CompostablesManager manager = MoreCompost.getInstance().getCompostablesManager();

		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(compostableTableId)
				.getConfigurationSection("compostables");

		CompostableTable compostableTable = manager.get(compostableTableId);

		List<Compostable> compostables = compostableTable.getCompostables().stream()
				.filter(compostable -> !compostable.getId().equals("default")).collect(Collectors.toList());
		List<Compostable> toList = compostables.stream()
				.filter(dropTable -> compostables.indexOf(dropTable) >= (page - 1) * COMPOSTABLES_NUMBER
						&& compostables.indexOf(dropTable) <= page * COMPOSTABLES_NUMBER - 1)
				.collect(Collectors.toList());

		int maxPage = (int) Math
				.ceil((compostables.size() > 0 ? compostables.size() : 1) / (double) COMPOSTABLES_NUMBER);

		this.inventory = Bukkit.createInventory(this, 54, title);
		toList.forEach(compostable -> registerClickConsumer(toList.indexOf(compostable),
				getCompostableItem(compostable), (e) -> {
					e.getWhoClicked().openInventory(
							new CompostableSettingsInventoryHolder(this, compostableTable.getId(), compostable.getId())
									.getInventory());
				}));

		registerClickConsumer(50, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));

		registerClickConsumer(51, getAddItem(),
				e -> e.getWhoClicked().openInventory(
						new StringSettingInventoryHolder(manager, section, null, null, e.getWhoClicked(), this)
								.getInventory()));

		if (page != maxPage) {
			registerClickConsumer(53, getNextPageItem(), e -> e.getWhoClicked().openInventory(
					new CompostablesListInventoryHolder(holder, title, compostableTableId, page + 1).getInventory()));
		}
		if (page != 1) {
			registerClickConsumer(52, getPreviousPageItem(), e -> e.getWhoClicked().openInventory(
					new CompostablesListInventoryHolder(holder, title, compostableTableId, page - 1).getInventory()));
		}
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private ItemStack getCompostableItem(Compostable compostable) {
		return getNamedItem(new ItemStack(compostable.getMaterial()), MessageUtils.color("&6&l" + compostable.getId()), new ArrayList<String>());
	}

}
