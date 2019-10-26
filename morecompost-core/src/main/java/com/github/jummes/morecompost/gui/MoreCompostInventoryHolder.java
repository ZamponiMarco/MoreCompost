package com.github.jummes.morecompost.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.gui.settings.SettingInventoryHolder;
import com.github.jummes.morecompost.managers.DataManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.collect.Lists;

public abstract class MoreCompostInventoryHolder implements InventoryHolder {

	private static final String BACK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJmMDQyNWQ2NGZkYzg5OTI5MjhkNjA4MTA5ODEwYzEyNTFmZTI0M2Q2MGQxNzViZWQ0MjdjNjUxY2JlIn19fQ=====";
	private static final String ADD_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjliODYxYWFiYjMxNmM0ZWQ3M2I0ZTU0MjgzMDU3ODJlNzM1NTY1YmEyYTA1MzkxMmUxZWZkODM0ZmE1YTZmIn19fQ======";
	private static final String REMOVE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY3NGQ0ZGI0Y2MzYmU0MWEzNzNkOWVmOWNhYzI3ZTYzNThjNTNmNjQxMTVkMTUwMjQzZjI1YWNmNjRmMmY1MCJ9fX0=======";
	private static final String ARROW_LEFT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY5N2MyNDg5MmNmYzAzYzcyOGZmYWVhYmYzNGJkZmI5MmQ0NTExNDdiMjZkMjAzZGNhZmE5M2U0MWZmOSJ9fX0=";
	private static final String ARROW_RIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZlMTQ1ZTcxMjk1YmNjMDQ4OGU5YmI3ZTZkNjg5NWI3Zjk2OWEzYjViYjdlYjM0YTUyZTkzMmJjODRkZjViIn19fQ===";

	
	private static final String BACK_ITEM_NAME = MessageUtils.color("&6&lGo back");
	private static final String ADD_ITEM_NAME = MessageUtils.color("&6&lLeft click &ato add a new object in this list");
	private static final String REMOVE_ITEM_NAME = MessageUtils
			.color("&6&lLeft click &ato remove this new object from the list");
	
	protected Inventory inventory;
	protected Map<Integer, Consumer<InventoryClickEvent>> clickMap;

	public MoreCompostInventoryHolder() {
		this.clickMap = new HashMap<>();
	}

	@Override
	public Inventory getInventory() {
		initializeInventory();
		return inventory;
	}

	protected abstract void initializeInventory();

	public void handleClickEvent(InventoryClickEvent e) {
		clickMap.get(e.getSlot()).accept(e);
		e.setCancelled(true);
	}

	public void registerClickConsumer(int slot, ItemStack item, Consumer<InventoryClickEvent> clickConsumer) {
		inventory.setItem(slot, item);
		clickMap.put(slot, clickConsumer);
	}

	public void fillInventoryWith(Material material) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) {
				registerClickConsumer(i, getPlaceholderItem(material), e -> {
				});
			}
		}
	}

	protected Consumer<InventoryClickEvent> getSettingConsumer(DataManager dataManager, ConfigurationSection section,
			String key, Object value, Class<? extends SettingInventoryHolder> clazz) {
		return e -> {
			if (e.getClick().equals(ClickType.LEFT)) {
				HumanEntity p = e.getWhoClicked();
				try {
					p.openInventory(clazz
							.getConstructor(DataManager.class, ConfigurationSection.class, String.class, Object.class,
									HumanEntity.class, InventoryHolder.class)
							.newInstance(dataManager, section, key, value, p, this).getInventory());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
	}

	protected ItemStack getNamedItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack getPlaceholderItem(Material material) {
		return getNamedItem(new ItemStack(material), " ");
	}

	protected ItemStack getBackItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(BACK_HEAD), BACK_ITEM_NAME);
	}

	protected ItemStack getAddItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ADD_HEAD), ADD_ITEM_NAME);
	}

	protected ItemStack getRemoveItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(REMOVE_HEAD), REMOVE_ITEM_NAME);
	}
	
	protected ItemStack getNextPageItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ARROW_RIGHT_HEAD), "&6&lNext page");
	}
	
	protected ItemStack getPreviousPageItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ARROW_LEFT_HEAD), "&6&lPrevious page");
	}

	protected ItemStack getSettingItem(ItemStack item, String key, Object value) {
		item = getNamedItem(item, MessageUtils.color(String.format("&6&l%s = &e&l%s", key, value.toString())));
		ItemMeta meta = item.getItemMeta();
		meta.setLore(getDefaultLore());
		item.setItemMeta(meta);
		return item;
	}

	private List<String> getDefaultLore() {
		return Lists.newArrayList(MessageUtils.color("&6&l- &e&lLeft click &6to modify"));
	}

}
