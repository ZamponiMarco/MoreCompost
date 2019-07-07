package com.github.jummes.morecompost.drops;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.jummes.morecompost.core.MoreCompost;

public class ItemCompostDrop implements CompostDrop {

	private static final String CUSTOM_KEY = "sasso";

	private ItemStack item;

	public ItemCompostDrop(ItemStack item) {
		this.item = item;
	}

	@Override
	public void dropLoot(Block block) {
		block.getWorld().spawn(block.getLocation().clone().add(.5, 1, .5), EntityType.DROPPED_ITEM.getEntityClass(),
				entity -> {
					Item item = (Item) entity;
					item.setItemStack(this.item);
					item.setMetadata(CUSTOM_KEY, new FixedMetadataValue(MoreCompost.getInstance(), true));
				});

	}

	@Override
	public void putInContainer(Block block) {
		Container container = (Container) block.getState();
		container.getInventory().addItem(item);
	}

}
