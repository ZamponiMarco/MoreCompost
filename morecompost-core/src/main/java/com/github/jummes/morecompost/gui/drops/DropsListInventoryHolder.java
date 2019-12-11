package com.github.jummes.morecompost.gui.drops;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.drops.AbstractCompostDrop;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;
import com.github.jummes.morecompost.gui.drops.factory.DropSettingsInventoryHolderFactory;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.DropsManager;
import com.google.common.collect.Lists;

public class DropsListInventoryHolder extends MoreCompostInventoryHolder {

	private static final int DROP_TABLES_NUMBER = 50;

	private InventoryHolder holder;
	private String title;
	private String dropTableId;
	private int page;

	public DropsListInventoryHolder(InventoryHolder holder, String title, String dropTableId, List<AbstractCompostDrop> drops,
			int page) {
		this.holder = holder;
		this.title = title;
		this.dropTableId = dropTableId;
		this.page = page;
	}

	@Override
	protected void initializeInventory() {
		DropsManager manager = MoreCompost.getInstance().getDropsManager();

		DropTable dropTable = manager.getDropTableById(dropTableId);
		
		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(dropTable.getId())
				.getConfigurationSection("drops");
		

		List<AbstractCompostDrop> drops = Lists.newArrayList(dropTable.getWeightMap().values());
		List<AbstractCompostDrop> toList = drops.stream().filter(drop -> drops.indexOf(drop) >= (page - 1) * DROP_TABLES_NUMBER
				&& drops.indexOf(drop) <= page * DROP_TABLES_NUMBER - 1).collect(Collectors.toList());

		int maxPage = (int) Math.ceil((drops.size() > 0 ? drops.size() : 1) / (double) DROP_TABLES_NUMBER);

		this.inventory = Bukkit.createInventory(this, 54, title);
		
		toList.forEach(drop -> registerClickConsumer(toList.indexOf(drop), getDropItem(drop),
				(e) -> e.getWhoClicked().openInventory(DropSettingsInventoryHolderFactory
						.buildDropSettingInventoryHolder(this, dropTable, drop).getInventory())));
		registerClickConsumer(50, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		registerClickConsumer(51, getAddItem(),
				e -> e.getWhoClicked().openInventory(
						new StringSettingInventoryHolder(manager, section, null, null, e.getWhoClicked(), this)
								.getInventory()));
		if (page != maxPage) {
			registerClickConsumer(53, getNextPageItem(), e -> e.getWhoClicked().openInventory(
					new DropsListInventoryHolder(holder, title, dropTableId, drops, page + 1).getInventory()));
		}
		if (page != 1) {
			registerClickConsumer(52, getPreviousPageItem(), e -> e.getWhoClicked().openInventory(
					new DropsListInventoryHolder(holder, title, dropTableId, drops, page - 1).getInventory()));
		}
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

	private ItemStack getDropItem(AbstractCompostDrop drop) {
		return drop.getGUIItem();
	}

}
