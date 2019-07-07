package com.github.jummes.morecompost.drops;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class ExperienceCompostDrop implements CompostDrop {

	private int amount;

	public ExperienceCompostDrop(int amount) {
		this.amount = amount;
	}

	@Override
	public void dropLoot(Block block) {
		ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 1, 0.5),
				EntityType.EXPERIENCE_ORB);
		orb.setExperience(amount);
	}

	@Override
	public void putInContainer(Block block) {
	}

}
