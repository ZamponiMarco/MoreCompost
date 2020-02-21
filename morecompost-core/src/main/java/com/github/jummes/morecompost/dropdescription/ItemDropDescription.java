package com.github.jummes.morecompost.dropdescription;

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@SerializableAs("ItemDropDescription")
public class ItemDropDescription extends DropDescription {

	private static final String ITEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E2ZTUzYmZiN2MxM2ZlZGJkZmU4OTY3NmY4MWZjMmNhNzk3NDYzNGE2ODQxNDFhZDFmNTE2NGYwZWRmNGEyIn19fQ==";
	private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19";

	@Serializable(headTexture = ITEM_HEAD, description = "gui.droptable.item-description.item")
	private ItemStackWrapper item;
	@Serializable(headTexture = COUNT_HEAD, description = "gui.droptable.item-description.count")
	private IntRange count;

	public ItemDropDescription() {
		this(new ItemStackWrapper(new ItemStack(Material.STONE)), new IntRange(1, 1));
	}

	@Override
	public void dropLoot(Block block) {
		ItemStack toDrop = item.getWrapped();
		Random random = new Random();
		toDrop.setAmount(random.nextInt(count.getDifference() + 1) + count.getMin());
		Location loc = block.getLocation().clone().add(0, .3, 0);
		block.getWorld().dropItemNaturally(loc, toDrop);
	}

	@Override
	public void putInContainer(Block block) {
		Container container = (Container) block.getState();
		ItemStack toDrop = item.getWrapped();
		toDrop.setAmount(new Random().nextInt(count.getDifference() + 1) + count.getMin());
		container.getInventory().addItem(toDrop);
	}

	public static ItemDropDescription deserialize(Map<String, Object> map) {
		ItemStackWrapper item = (ItemStackWrapper) map.get("item");
		IntRange count = (IntRange) map.get("count");
		return new ItemDropDescription(item, count);
	}

	@Override
	public ItemStack getGUIItem() {
		return item.getWrapped();
	}

}
