package com.github.jummes.morecompost.gui;

import java.util.ArrayList;
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
import com.github.jummes.morecompost.locales.LocaleString;
import com.github.jummes.morecompost.managers.DataManager;
import com.github.jummes.morecompost.managers.LocalesManager;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

public abstract class MoreCompostInventoryHolder implements InventoryHolder {

	private static final String BACK_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJmMDQyNWQ2NGZkYzg5OTI5MjhkNjA4MTA5ODEwYzEyNTFmZTI0M2Q2MGQxNzViZWQ0MjdjNjUxY2JlIn19fQ=====";
	private static final String ADD_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjliODYxYWFiYjMxNmM0ZWQ3M2I0ZTU0MjgzMDU3ODJlNzM1NTY1YmEyYTA1MzkxMmUxZWZkODM0ZmE1YTZmIn19fQ======";
	private static final String REMOVE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY3NGQ0ZGI0Y2MzYmU0MWEzNzNkOWVmOWNhYzI3ZTYzNThjNTNmNjQxMTVkMTUwMjQzZjI1YWNmNjRmMmY1MCJ9fX0=======";
	private static final String ARROW_LEFT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY5N2MyNDg5MmNmYzAzYzcyOGZmYWVhYmYzNGJkZmI5MmQ0NTExNDdiMjZkMjAzZGNhZmE5M2U0MWZmOSJ9fX0=";
	private static final String ARROW_RIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZlMTQ1ZTcxMjk1YmNjMDQ4OGU5YmI3ZTZkNjg5NWI3Zjk2OWEzYjViYjdlYjM0YTUyZTkzMmJjODRkZjViIn19fQ===";

	protected Inventory inventory;
	protected Map<Integer, Consumer<InventoryClickEvent>> clickMap;
	protected VersionWrapper wrapper;
	protected LocalesManager localesManager;

	public MoreCompostInventoryHolder() {
		this.clickMap = new HashMap<>();
		this.wrapper = MoreCompost.getInstance().getWrapper();
		this.localesManager = MoreCompost.getInstance().getLocalesManager();
	}

	protected abstract void initializeInventory();

	/**
	 * Handles the InventoryClickEvent depending on the slot that has been clicked
	 * and the content of the clickMap
	 * 
	 * @param e the event that has been fired
	 */
	public void handleClickEvent(InventoryClickEvent e) {
		clickMap.get(e.getSlot()).accept(e);
		e.setCancelled(true);
	}

	/**
	 * Register an event consumer on a determined slot and puts an item in such slot
	 * 
	 * @param slot          slot that the event will be linked to
	 * @param item          item that will be put in the inventory in the slot
	 * @param clickConsumer event consumer that will be called when the event is
	 *                      fired
	 */
	public void registerClickConsumer(int slot, ItemStack item, Consumer<InventoryClickEvent> clickConsumer) {
		inventory.setItem(slot, item);
		clickMap.put(slot, clickConsumer);
	}

	/**
	 * Registers the consumer of a setting that can change the representation of
	 * object in the data files.
	 * 
	 * @param slot        slot that the event will be linked to
	 * @param dataManager data manager of the represented object
	 * @param section     section of configuration representing the object
	 * @param item        item that will be displayed in inventory
	 * @param key         key of the value
	 * @param value       value associated to the key
	 * @param description description of the attribute
	 * @param clazz       class that manages the update of data
	 */
	public void registerSettingConsumer(int slot, DataManager dataManager, ConfigurationSection section, ItemStack item,
			String key, Object value, List<String> description, Class<? extends SettingInventoryHolder> clazz,
			boolean resettable) {
		registerClickConsumer(slot, getSettingItem(item, key, value, description, resettable),
				getSettingConsumer(dataManager, section, key, value, clazz, resettable));
	}

	/**
	 * Fills the inventory with a material with an empty name
	 * 
	 * @param material
	 */
	public void fillInventoryWith(Material material) {
		for (int i = 0; i < inventory.getSize(); i++) {
			if (inventory.getItem(i) == null) {
				registerClickConsumer(i, getNotNamedItem(material), e -> {
				});
			}
		}
	}

	/**
	 * Gets a named item
	 * 
	 * @param item item that works as the base
	 * @param name name that will be displayed
	 * @return
	 */
	protected ItemStack getNamedItem(ItemStack item, String name, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(MessageUtils.color(name));
		meta.setLore(null);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private Consumer<InventoryClickEvent> getSettingConsumer(DataManager manager, ConfigurationSection section,
			String key, Object value, Class<? extends SettingInventoryHolder> clazz, boolean resettable) {
		return e -> {
			if (e.getClick().equals(ClickType.LEFT)) {
				HumanEntity p = e.getWhoClicked();
				try {
					p.openInventory(clazz
							.getConstructor(DataManager.class, ConfigurationSection.class, String.class, Object.class,
									HumanEntity.class, InventoryHolder.class)
							.newInstance(manager, section, key, value, p, this).getInventory());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (e.getClick().equals(ClickType.RIGHT) && resettable) {
				section.set(key, null);
				manager.saveAndReloadData();
				e.getWhoClicked().openInventory(this.getInventory());
			}
		};
	}

	private ItemStack getSettingItem(ItemStack item, String key, Object value, List<String> description,
			boolean resettable) {
		item = getNamedItem(item, MessageUtils.color(String.format("&6&l%s = &e&l%s", key, value.toString())),
				getSettingLore(description, resettable));
		return item;
	}

	private List<String> getSettingLore(List<String> description, boolean resettable) {
		if (!description.contains(localesManager.getSingleLocaleString(LocaleString.MODIFY_ATTRIBUTE))) {
			description.add(0, localesManager.getSingleLocaleString(LocaleString.MODIFY_ATTRIBUTE));
		}
		if (resettable && !description.contains(localesManager.getSingleLocaleString(LocaleString.RESET_ATTRIBUTE))) {
			description.add(1, localesManager.getSingleLocaleString(LocaleString.RESET_ATTRIBUTE));
		}
		return description;
	}

	private ItemStack getNotNamedItem(Material material) {
		return getNamedItem(new ItemStack(material), " ", new ArrayList<String>());
	}

	protected ItemStack getBackItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(BACK_HEAD),
				localesManager.getSingleLocaleString(LocaleString.BACK_ITEM_NAME), new ArrayList<String>());
	}

	protected ItemStack getAddItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ADD_HEAD),
				localesManager.getSingleLocaleString(LocaleString.ADD_ITEM_NAME), new ArrayList<String>());
	}

	protected ItemStack getRemoveItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(REMOVE_HEAD),
				localesManager.getSingleLocaleString(LocaleString.REMOVE_ITEM_NAME), new ArrayList<String>());
	}

	protected ItemStack getNextPageItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ARROW_RIGHT_HEAD),
				localesManager.getSingleLocaleString(LocaleString.NEXT_PAGE_ITEM_NAME), new ArrayList<String>());
	}

	protected ItemStack getPreviousPageItem() {
		return getNamedItem(MoreCompost.getInstance().getWrapper().skullFromValue(ARROW_LEFT_HEAD),
				localesManager.getSingleLocaleString(LocaleString.PREVIOUS_PAGE_ITEM_NAME), new ArrayList<String>());
	}

	@Override
	public Inventory getInventory() {
		initializeInventory();
		return inventory;
	}

}
