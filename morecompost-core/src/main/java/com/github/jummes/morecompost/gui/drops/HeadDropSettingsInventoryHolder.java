package com.github.jummes.morecompost.gui.drops;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.drops.HeadCompostDrop;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.settings.IntegerSettingInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

public class HeadDropSettingsInventoryHolder extends DropSettingsInventoryHolder {

	private static final String WEIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVlOTE1MmVmZDg5MmY2MGQ3ZTBkN2U1MzM2OWUwNDc3OWVkMzExMWUyZmIyNzUyYjZmNGMyNmRmNTQwYWVkYyJ9fX0=";
	private static final String MINIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MmM5ZDYyOGJiMzIyMWVmMzZiNGNiZDBiOWYxNWVkZDU4ZTU4NjgxODUxNGQ3ZTgyM2Q1NWM0OGMifX19=";
	private static final String MAXIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUxNDlkZGRhZGVkMjBkMjQ0ZTBiYjYyYTJkOWZhMGRjNmM2YTc4NjI1NTkzMjhhOTRmNzc3MjVmNTNjMzU4In19fQ===";
	private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0======";
	private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRhOWZiMzU1MmQ1NTE1NTNkOWRkNDNjMmJiMWQyNjg4OTNkZjY4ZDczZTQ2MTEzNDNiNTcyYWU2NDI1Y2EifX19";

	public HeadDropSettingsInventoryHolder(String dropTableId, String dropId, InventoryHolder holder) {
		super(dropTableId, dropId, holder);
	}

	@Override
	protected void initializeInventory() {
		VersionWrapper wrapper = MoreCompost.getInstance().getWrapper();
		
		DropsManager manager = MoreCompost.getInstance().getDropsManager();

		DropTable dropTable = manager.get(dropTableId);
		HeadCompostDrop drop = (HeadCompostDrop) dropTable.get(dropId);

		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(dropTable.getId())
				.getConfigurationSection("drops").getConfigurationSection(drop.getId());

		this.inventory = Bukkit.createInventory(this, 27,
				MessageUtils.color(String.format("&6&lDrop: &1&l%s", drop.getId())));

		registerClickConsumer(3, getSettingItem(wrapper.skullFromValue(TYPE_HEAD), "type", drop.getType()),
				getSettingConsumer(manager, section, "type", drop.getType(), StringSettingInventoryHolder.class));
		registerClickConsumer(5, getSettingItem(wrapper.skullFromValue(WEIGHT_HEAD), "weight", drop.getWeight()),
				getSettingConsumer(manager, section, "weight", drop.getWeight(), IntegerSettingInventoryHolder.class));
		registerClickConsumer(11,
				getSettingItem(wrapper.skullFromValue(MINIMUM_HEAD), "minCount", drop.getMinCount()),
				getSettingConsumer(manager, section, "minCount", drop.getMinCount(),
						IntegerSettingInventoryHolder.class));
		registerClickConsumer(12,
				getSettingItem(wrapper.skullFromValue(MAXIMUM_HEAD), "maxCount", drop.getMaxCount()),
				getSettingConsumer(manager, section, "maxCount", drop.getMaxCount(),
						IntegerSettingInventoryHolder.class));
		registerClickConsumer(14, getSettingItem(drop.getGUIItem(), "texture", drop.getTexture()),
				getSettingConsumer(manager, section, "texture", drop.getTexture(), StringSettingInventoryHolder.class));
		registerClickConsumer(15,
				getSettingItem(wrapper.skullFromValue(NAME_HEAD), "displayName",
						drop.getItem().getItemMeta().getDisplayName()),
				getSettingConsumer(manager, section, "displayName", drop.getItem().getItemMeta().getDisplayName(),
						StringSettingInventoryHolder.class));
		registerClickConsumer(18, getRemoveItem(), e -> {
			section.getParent().set(dropId, null);
			manager.reloadData();
			e.getWhoClicked().openInventory(holder.getInventory());
		});
		registerClickConsumer(26, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);

	}

}
