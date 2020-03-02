package com.github.jummes.morecompost.compostable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.core.MoreCompost;
import com.google.common.collect.Lists;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SerializableAs("Compostable")
public class Compostable implements Model {

	private static final String METADATA_KEY = "forcedDropTableId";

	private static final String CHANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0===";
	private static final String ROLLS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19=";
	private static final String ITEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTI5ZWE2OGEwYzYxYjFlZGEyZDhhZWMzZTIyMjk3MjczMjNiN2QyZGE2YmMwNGNjMGNkMmRlZjNiNDcxMiJ9fX0====";
	private static final String FORCED_DROPTABLE_ID = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc0ZDEzYjUxMDE2OGM3YWNiNDRiNjQ0MTY4NmFkN2FiMWNiNWI3NDg4ZThjZGY5ZDViMjJiNDdjNDgzZjIzIn19fQ======";

	@EqualsAndHashCode.Include
	@Serializable(headTexture = ITEM_HEAD, description = "gui.compostabletable.item")
	private ItemStackWrapper item;
	@Serializable(headTexture = ROLLS_HEAD, description = "gui.compostabletable.rolls")
	private IntRange rolls;
	@Serializable(headTexture = CHANCE_HEAD, description = "gui.compostabletable.chance")
	private double chance;
	@Serializable(headTexture = FORCED_DROPTABLE_ID, description = "gui.compostabletable.forced")
	private String forcedDropTableId;
	private boolean isDefault;
	private Random random;

	public Compostable() {
		this(new ItemStackWrapper(new ItemStack(Material.STONE)), new IntRange(1, 1), 0.5, null, false);
	}

	public Compostable(ItemStackWrapper item, IntRange rolls, double chance, String forcedDropTableId,
			boolean isDefault) {
		this.item = item;
		this.rolls = rolls;
		this.chance = chance;
		this.forcedDropTableId = forcedDropTableId;
		this.isDefault = isDefault;
		this.random = new Random();
	}

	/**
	 * Composts a block
	 * 
	 * @param block block to be composted
	 * @return true if block was succesfully composted, false otherwise
	 */
	public boolean compost(Block block) {
		if (block.getType().equals(Material.COMPOSTER)) {
			Levelled composter = (Levelled) block.getBlockData();

			if (composter.getLevel() == 0 && forcedDropTableId != null) {
				block.setMetadata(METADATA_KEY, new FixedMetadataValue(MoreCompost.getInstance(), forcedDropTableId));
			}

			if (block.hasMetadata(METADATA_KEY) && (forcedDropTableId == null || (forcedDropTableId != null
					&& !block.getMetadata(METADATA_KEY).get(0).asString().equals(this.forcedDropTableId)))) {
				block.removeMetadata(METADATA_KEY, MoreCompost.getInstance());
			}

			int maxLevel = composter.getMaximumLevel() - 1;
			AtomicBoolean hasFilled = new AtomicBoolean(false);

			if (composter.getLevel() != maxLevel) {
				int randomRolls = random.nextInt(rolls.getDifference() + 1) + rolls.getMin();
				IntStream.range(0, randomRolls).forEach(i -> {
					if (composter.getLevel() != maxLevel) {
						int currLevel = composter.getLevel();
						if (currLevel < maxLevel && new Random().nextDouble() < chance) {
							composter.setLevel(++currLevel);
							hasFilled.set(true);
						}
						block.setBlockData(composter);
					}
				});
				block.getWorld().playSound(block.getLocation(),
						hasFilled.get() ? Sound.BLOCK_COMPOSTER_FILL_SUCCESS : Sound.BLOCK_COMPOSTER_FILL, 1, 1);
			}
			return hasFilled.get();
		}
		return false;
	}

	public static Compostable deserialize(Map<String, Object> map) {
		ItemStackWrapper item = (ItemStackWrapper) map.get("item");
		IntRange rolls = (IntRange) map.get("rolls");
		double chance = (double) map.get("chance");
		String forcedDropTableId = (String) map.get("forcedDropTableId");
		return new Compostable(item, rolls, chance, forcedDropTableId, false);
	}

	@Override
	public ItemStack getGUIItem() {
		if (isDefault) {
			return null;
		}
		ItemStack item = this.item.getWrapped().clone();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
		lore.addAll(Lists.newArrayList(MessageUtils.color("&7Chance → &8&l" + String.valueOf(chance)),
				MessageUtils.color("&7Rolls → &8&l" + rolls), MessageUtils.color("&6&lLeft click &eto modify."),
				MessageUtils.color("&6&lRight click &eto delete.")));
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
