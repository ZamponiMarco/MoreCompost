package com.github.jummes.morecompost.drops;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.utils.MessageUtils;

public class HeadCompostDrop implements CompostDrop {

	private String id;
	private int weight;
	private ItemStack item;
	private int minCount;
	private int maxCount;
	private String texture;

	public HeadCompostDrop(String id, int weight, ItemStack item, int minCount, int maxCount, String texture) {
		this.id = id;
		this.weight = weight;
		this.item = item;
		this.minCount = minCount;
		this.maxCount = maxCount;
		this.texture = texture;
	}

	@Override
	public void dropLoot(Block block) {
		int difference = maxCount - minCount;
		this.item.setAmount(new Random().nextInt(difference + 1) + minCount);

		MoreCompost.getInstance().getWrapper().dropKeyedItem(block.getLocation(), item);
	}

	@Override
	public void putInContainer(Block block) {
		Container container = (Container) block.getState();
		container.getInventory().addItem(item);
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ItemStack getGUIItem() {
		ItemStack toReturn = new ItemStack(item.getType());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(MessageUtils.color("&6&l" + getId()));
		toReturn.setItemMeta(meta);
		return toReturn;
	}

	@Override
	public String getType() {
		return "head";
	}

	public ItemStack getItem() {
		return item;
	}

	public int getMinCount() {
		return minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public String getTexture() {
		return texture.substring(0, 7).concat("...");
	}

}
