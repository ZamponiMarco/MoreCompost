package com.github.jummes.morecompost.compostable;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;

public enum DefaultCompostable {

	BEETROOT_SEEDS(.3), DRIED_KELP(.3), GRASS(.3), KELP(.3), ACACIA_LEAVES(.3), BIRCH_LEAVES(.3), DARK_OAK_LEAVES(.3),
	JUNGLE_LEAVES(.3), OAK_LEAVES(.3), SPRUCE_LEAVES(.3), MELON_SEEDS(.3), PUMPKIN_SEEDS(.3), ACACIA_SAPLING(.3),
	BIRCH_SAPLING(.3), DARK_OAK_SAPLING(.3), JUNGLE_SAPLING(.3), OAK_SAPLING(.3), SPRUCE_SAPLING(.3), SEAGRASS(.3),
	SWEET_BERRIES(.3), WHEAT_SEEDS(.3), CACTUS(.5), DRIED_KELP_BLOCK(.5), MELON_SLICE(.5), SUGAR_CANE(.5),
	TALL_GRASS(.5), VINE(.5), APPLE(.65), BEETROOT(.65), CARROT(.65), COCOA_BEANS(.65), FERN(.65), LARGE_FERN(.65),
	DANDELION(.65), POPPY(.65), BLUE_ORCHID(.65), ALLIUM(.65), AZURE_BLUET(.65), RED_TULIP(.65), ORANGE_TULIP(.65),
	WHITE_TULIP(.65), PINK_TULIP(.65), OXEYE_DAISY(.65), CORNFLOWER(.65), LILY_OF_THE_VALLEY(.65), WITHER_ROSE(.65),
	SUNFLOWER(.65), LILAC(.65), ROSE_BUSH(.65), PEONY(.65), LILY_PAD(.65), MELON(.65), RED_MUSHROOM(.65),
	BROWN_MUSHROOM(.65), POTATO(.65), PUMPKIN(.65), CARVED_PUMPKIN(.65), SEA_PICKLE(.65), WHEAT(.65), BAKED_POTATO(.85),
	BREAD(.85), COOKIE(.85), HAY_BLOCK(.85), BROWN_MUSHROOM_BLOCK(.85), RED_MUSHROOM_BLOCK(.85), CAKE(1),
	PUMPKIN_PIE(1);

	private ItemStack item;
	private double percentage;

	private DefaultCompostable(double percentage) {
		this.item = new ItemStack(Material.valueOf(this.name()));
		this.percentage = percentage;
	}

	public Compostable getCompostable() {
		return new Compostable(new ItemStackWrapper(item), new IntRange(1, 1), percentage, null, true);
	}

	public static boolean isDefaultCompostable(Compostable compostable) {
		return Arrays.stream(values())
				.anyMatch(defaultCompostable -> defaultCompostable.getCompostable().equals(compostable));
	}

}
