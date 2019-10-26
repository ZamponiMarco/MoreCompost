package com.github.jummes.morecompost.drops.factory;

import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.HeadCompostDrop;
import com.github.jummes.morecompost.utils.MessageUtils;

public class HeadCompostDropFactory implements CompostDropFactory {

	private static final String DEFAULT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWIzYjFmNzg1ZjAxNzUzYzQ1ZWY5N2ZjZmZmZmIzZjUyNjU4ZmZjZWIxN2FkM2Y3YjU5Mjk0NWM2ZGYyZmEifX19";

	@Override
	public CompostDrop buildCompostDrop(ConfigurationSection drop) {

		int weight = drop.getInt("weight", 1);

		int minCount = drop.getInt("minCount", 1);

		int maxCount = drop.getInt("maxCount", 1);

		String texture = drop.getString("texture", DEFAULT_HEAD);

		ItemStack item = MoreCompost.getInstance().getWrapper().skullFromValue(texture);

		ItemMeta meta = item.getItemMeta();

		String displayName = drop.getString("displayName");

		meta.setDisplayName(displayName == null ? "" : MessageUtils.color(displayName));

		meta.setLore(drop.getStringList("lore").stream().map(MessageUtils::color).collect(Collectors.toList()));

		item.setItemMeta(meta);
		return new HeadCompostDrop(drop.getName(), weight, item, minCount, maxCount, texture);
	}

}
