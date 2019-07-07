package com.github.jummes.morecompost.drops.factory;

import java.util.Random;
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

		Material material = Material.valueOf(drop.getString("material", "DIRT").toUpperCase());

		int count = new Random().nextInt(drop.getInt("maxCount", 1)) + 1;

		ItemStack item = new ItemStack(material, count);

		ItemMeta meta = item.getItemMeta();

		String displayName = drop.getString("displayName");

		meta.setDisplayName(displayName == null ? "" : MessageUtils.color(displayName));

		meta.setLore(drop.getStringList("lore").stream().map(MessageUtils::color).collect(Collectors.toList()));

		item.setItemMeta(meta);
		return new ItemCompostDrop(item);
	}

}
