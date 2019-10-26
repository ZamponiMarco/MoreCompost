package com.github.jummes.morecompost.compostables;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;

public class Compostable {

	private String id;
	private Material material;
	private int minRolls;
	private int maxRolls;
	private double chance;
	private Random random;

	public Compostable(String id, Material material, int minRolls, int maxRolls, double chance) {
		this.id = id;
		this.material = material;
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
		this.chance = chance;
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
			AtomicBoolean hasFilled = new AtomicBoolean(false);
			int maxLevel = composter.getMaximumLevel() - 1;

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

}
