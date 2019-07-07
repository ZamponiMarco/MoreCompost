package com.github.jummes.morecompost.droptables;

import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.bukkit.block.Block;

import com.github.jummes.morecompost.drops.CompostDrop;

public class DropTable {

	private int minRolls;
	private int maxRolls;
	private Map<Integer, CompostDrop> weightMap;
	private Random random;

	public DropTable(int minRolls, int maxRolls, Map<Integer, CompostDrop> weightMap) {
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
		this.weightMap = weightMap;
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

}
