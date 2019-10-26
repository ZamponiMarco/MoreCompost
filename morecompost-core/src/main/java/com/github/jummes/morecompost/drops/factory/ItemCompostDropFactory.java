package com.github.jummes.morecompost.drops.factory;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.ItemCompostDrop;
import com.github.jummes.morecompost.utils.MessageUtils;

public class ItemCompostDropFactory implements CompostDropFactory {

	@Override
	public CompostDrop buildCompostDrop(ConfigurationSection drop) {

		int weight = drop.getInt("weight", 1);

		Material material = Material.valueOf(drop.getString("material", "DIRT").toUpperCase());

		int minCount = drop.getInt("minCount", 1);

		int maxCount = drop.getInt("maxCount", 1);

		ItemStack item = new ItemStack(material);

		ItemMeta meta = item.getItemMeta();

		String displayName = drop.getString("displayName");

		meta.setDisplayName(displayName == null ? "" : MessageUtils.color(displayName));

		meta.setLore(drop.getStringList("lore").stream().map(MessageUtils::color).collect(Collectors.toList()));

		item.setItemMeta(meta);
		return new ItemCompostDrop(drop.getName(), weight, item, minCount, maxCount);
	}

}
