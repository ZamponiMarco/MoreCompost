package com.github.jummes.morecompost.compostables;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.jummes.morecompost.core.MoreCompost;

public class Compostable {

	private static final String METADATA_KEY = "forcedDropTableId";

	private String id;
	private Material material;
	private int minRolls;
	private int maxRolls;
	private double chance;
	private Optional<String> forcedDropTableId;
	private Random random;

	public Compostable(String id, Material material, int minRolls, int maxRolls, double chance,
			Optional<String> forcedDropTableId) {
		this.id = id;
		this.material = material;
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
		this.chance = chance;
		this.forcedDropTableId = forcedDropTableId;
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

			if (composter.getLevel() == 0 && forcedDropTableId.isPresent()) {
				block.setMetadata(METADATA_KEY,
						new FixedMetadataValue(MoreCompost.getInstance(), forcedDropTableId.get()));
			}

			if (block.hasMetadata(METADATA_KEY) && (!forcedDropTableId.isPresent() || (forcedDropTableId.isPresent()
					&& !block.getMetadata(METADATA_KEY).get(0).asString().equals(this.forcedDropTableId.get())))) {
				block.removeMetadata(METADATA_KEY, MoreCompost.getInstance());
			}
			
			int maxLevel = composter.getMaximumLevel() - 1;
			AtomicBoolean hasFilled = new AtomicBoolean(false);

			if (composter.getLevel() != maxLevel) {
				int difference = maxRolls - minRolls;
				int rolls = random.nextInt(difference + 1) + minRolls;
				IntStream.range(0, rolls).forEach(i -> {
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

	public Material getMaterial() {
		return material;
	}

	public String getId() {
		return id;
	}

	public double getChance() {
		return chance;
	}

	public int getMinRolls() {
		return minRolls;
	}

	public int getMaxRolls() {
		return maxRolls;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((material == null) ? 0 : material.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compostable other = (Compostable) obj;
		if (material != other.material)
			return false;
		return true;
	}

	public Optional<String> getForcedDropTableId() {
		return forcedDropTableId;
	}

}
