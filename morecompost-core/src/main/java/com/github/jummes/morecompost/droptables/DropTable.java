package com.github.jummes.morecompost.droptables;

import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.bukkit.block.Block;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.drops.CompostDrop;

public class DropTable {

	private static final String PERM_PREFIX = "morecompost.drops.";

	private Permission permission;
	private int minRolls;
	private int maxRolls;
	private int priority;
	private Map<Integer, CompostDrop> weightMap;
	private boolean presentInConfig;
	private Random random;

	public DropTable(Permission permission, int minRolls, int maxRolls, int priority,
			Map<Integer, CompostDrop> weightMap, boolean presentInConfig) {
		this.permission = permission;
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
		this.priority = priority;
		this.weightMap = weightMap;
		this.presentInConfig = presentInConfig;
		this.random = new Random();
	}

	public void dropAllLoot(Block block) {
		int difference = maxRolls - minRolls;
		int rolls = random.nextInt(difference + 1) + minRolls;
		IntStream.range(0, rolls).forEach(i -> dropRandomLoot(block));
	}

	private void dropRandomLoot(Block block) {
		TreeSet<Integer> set = new TreeSet<>(weightMap.keySet());
		weightMap.get(set.higher(random.nextInt(set.last()))).dropLoot(block);
	}

	public void fillContainer(Block block) {
		int difference = maxRolls - minRolls;
		int rolls = random.nextInt(difference + 1) + minRolls;
		IntStream.range(0, rolls).forEach(i -> fillContainerRandom(block));
	}

	private void fillContainerRandom(Block block) {
		TreeSet<Integer> set = new TreeSet<>(weightMap.keySet());
		weightMap.get(set.higher(random.nextInt(set.last()))).putInContainer(block);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getId() {
		return permission.getName().substring(PERM_PREFIX.length(), permission.getName().length());
	}

	public int getMinRolls() {
		return minRolls;
	}

	public int getMaxRolls() {
		return maxRolls;
	}

	public Map<Integer, CompostDrop> getWeightMap() {
		return weightMap;
	}

	public CompostDrop getDropById(String dropId) {
		return weightMap.values().stream().filter(drop -> drop.getId().equals(dropId)).findFirst().get();
	}

	public boolean isPresentInConfig() {
		return presentInConfig;
	}

}
