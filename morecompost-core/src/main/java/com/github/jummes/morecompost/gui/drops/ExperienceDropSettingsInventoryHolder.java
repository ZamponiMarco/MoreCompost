package com.github.jummes.morecompost.gui.drops;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.drops.ExperienceCompostDrop;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.settings.IntegerSettingInventoryHolder;
import com.github.jummes.morecompost.gui.settings.StringSettingInventoryHolder;
import com.github.jummes.morecompost.locales.LocaleString;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.utils.MessageUtils;

public class ExperienceDropSettingsInventoryHolder extends DropSettingsInventoryHolder {

	private static final String WEIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVlOTE1MmVmZDg5MmY2MGQ3ZTBkN2U1MzM2OWUwNDc3OWVkMzExMWUyZmIyNzUyYjZmNGMyNmRmNTQwYWVkYyJ9fX0=";
	private static final String MINIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3MmM5ZDYyOGJiMzIyMWVmMzZiNGNiZDBiOWYxNWVkZDU4ZTU4NjgxODUxNGQ3ZTgyM2Q1NWM0OGMifX19=";
	private static final String MAXIMUM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTUxNDlkZGRhZGVkMjBkMjQ0ZTBiYjYyYTJkOWZhMGRjNmM2YTc4NjI1NTkzMjhhOTRmNzc3MjVmNTNjMzU4In19fQ===";
	private static final String TYPE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRhOWZiMzU1MmQ1NTE1NTNkOWRkNDNjMmJiMWQyNjg4OTNkZjY4ZDczZTQ2MTEzNDNiNTcyYWU2NDI1Y2EifX19";

	public ExperienceDropSettingsInventoryHolder(String dropTableId, String dropId, InventoryHolder holder) {
		super(dropTableId, dropId, holder);
	}

	@Override
	protected void initializeInventory() {
		DropsManager manager = MoreCompost.getInstance().getDropsManager();

		DropTable dropTable = manager.getDropTableById(dropTableId);
		ExperienceCompostDrop drop = (ExperienceCompostDrop) dropTable.getDropById(dropId);

		ConfigurationSection section = manager.getDataYaml().getConfigurationSection(dropTable.getId())
				.getConfigurationSection("drops").getConfigurationSection(drop.getId());

		this.inventory = Bukkit.createInventory(this, 27,
				MessageUtils.color(String.format("&6&lDrop: &1&l%s", drop.getId())));
		registerSettingConsumer(3, manager, section, wrapper.skullFromValue(TYPE_HEAD), "type", drop.getType(),
				localesManager.getLocaleString(LocaleString.TYPE_DESCRIPTION), StringSettingInventoryHolder.class, true);
		registerSettingConsumer(5, manager, section, wrapper.skullFromValue(WEIGHT_HEAD), "weight", drop.getWeight(),
				localesManager.getLocaleString(LocaleString.WEIGHT_DESCRIPTION), IntegerSettingInventoryHolder.class, true);
		registerSettingConsumer(12, manager, section, wrapper.skullFromValue(MINIMUM_HEAD), "minAmount",
				drop.getMinAmount(), localesManager.getLocaleString(LocaleString.MIN_AMOUNT_DESCRIPTION),
				IntegerSettingInventoryHolder.class, true);
		registerSettingConsumer(14, manager, section, wrapper.skullFromValue(MAXIMUM_HEAD), "maxAmount",
				drop.getMaxAmount(), localesManager.getLocaleString(LocaleString.MAX_AMOUNT_DESCRIPTION),
				IntegerSettingInventoryHolder.class, true);
		registerClickConsumer(18, getRemoveItem(), e -> {
			section.getParent().set(dropId, null);
			manager.saveAndReloadData();
			e.getWhoClicked().openInventory(holder.getInventory());
		});
		registerClickConsumer(26, getBackItem(), e -> e.getWhoClicked().openInventory(holder.getInventory()));
		fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
	}

}
