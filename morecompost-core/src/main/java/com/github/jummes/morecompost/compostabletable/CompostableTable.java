package com.github.jummes.morecompost.compostabletable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.compostable.Compostable;
import com.github.jummes.morecompost.compostable.DefaultCompostable;
import com.google.common.collect.Lists;

import lombok.Getter;

@Getter
@SerializableAs("CompostableTable")
public class CompostableTable implements Model {

	private static int currentPriority = 10000;

	private static final String PERM_PREFIX = "morecompost.compostables.";

	private static final String PERMISSION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19=";
	private static final String PRIORITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I4N2QyMjUyY2FjMWFhMTVkZjMyNTk5OGI4ZWM4MmVmOTEwOWI2YzU2NzYxMGFmYWMwZWNkYTUxM2Y2MSJ9fX0==";
	private static final String REPLACE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0==";
	private static final String COMPOSTABLES_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTkyMDNlYzgyNTU1NGEwMmQ4NTAxZTMzNThhMGFhZjg5N2NiNTc5MGRjYjFjZjdiMTkzNGI1MWUyZDQ2YjNlNiJ9fX0==";

	@Serializable(headTexture = PERMISSION_HEAD, description = "gui.compostabletable.permission")
	private String permissionString;
	@Serializable(headTexture = PRIORITY_HEAD, description = "gui.compostabletable.priority")
	private int priority;
	@Serializable(headTexture = COMPOSTABLES_HEAD, description = "gui.compostabletable.compostables")
	private List<Compostable> compostables;
	@Serializable(headTexture = REPLACE_HEAD, description = "gui.compostabletable.replace")
	private boolean replaceDefaultCompostables;

	public CompostableTable() {
		this("Tier" + String.valueOf(currentPriority), currentPriority++, new ArrayList<Compostable>(), false);
	}

	public CompostableTable(String permissionString, int priority, List<Compostable> compostables,
			boolean replaceDefaultCompostables) {
		super();
		this.permissionString = permissionString;
		this.priority = priority;
		this.compostables = compostables;
		this.replaceDefaultCompostables = replaceDefaultCompostables;
		if (!replaceDefaultCompostables) {
			Arrays.stream(DefaultCompostable.values())
					.filter(compostable -> !compostables.contains(compostable.getCompostable()))
					.forEach(compostable -> compostables.add(compostable.getCompostable()));
		}
	}

	/**
	 * Checks if the material is present in the compostable set, if it is, tries to
	 * compost the block
	 * 
	 * @param block    block to compost
	 * @param material compostable material
	 * @return true if the compostable is contained inside the compostable table
	 */
	public boolean compost(Block block, ItemStack item) {
		AtomicBoolean isPresent = new AtomicBoolean(false);
		compostables.stream().filter(compostable -> compostable.getItem().getWrapped().isSimilar(item)).findFirst()
				.ifPresent(compostable -> {
					compostable.compost(block);
					isPresent.set(true);
				});
		return isPresent.get();
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("==", "CompostableTable");
		map.put("permissionString", permissionString);
		map.put("priority", priority);
		List<Compostable> filteredCompostables = compostables.stream()
				.filter(compostable -> !compostable.isDefault())
				.collect(Collectors.toList());
		map.put("compostables", filteredCompostables);
		map.put("replaceDefaultCompostables", replaceDefaultCompostables);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static CompostableTable deserialize(Map<String, Object> map) {
		String permissionString = (String) map.get("permissionString");
		int priority = (int) map.get("priority");
		currentPriority = Math.max(currentPriority, priority + 1);
		List<Compostable> compostables = (List<Compostable>) map.get("compostables");
		boolean replaceDefaultCompostables = (boolean) map.get("replaceDefaultCompostables");
		return new CompostableTable(permissionString, priority, compostables, replaceDefaultCompostables);
	}

	@Override
	public ItemStack getGUIItem() {
		return ItemUtils.getNamedItem(new ItemStack(Material.COMPOSTER), "&6&lPermission → &c&l" + permissionString,
				Lists.newArrayList(MessageUtils.color("&7Priority → &8&l" + String.valueOf(priority)),
						MessageUtils.color("&6&lLeft click &eto modify."),
						MessageUtils.color("&6&lRight click &eto delete.")));
	}

	public String getPermissionString() {
		return PERM_PREFIX.concat(permissionString);
	}
}
