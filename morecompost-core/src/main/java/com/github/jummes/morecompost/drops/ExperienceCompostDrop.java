package com.github.jummes.morecompost.drops;

import java.util.Random;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.utils.MessageUtils;

public class ExperienceCompostDrop implements CompostDrop {

	private static final String EXPERIENCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg5MDFmNzE0MzRkNTM5MjA3NDc2OTRmNjgyZjVlNTNiOGY3NDQ4M2YyNjljMzg0YzY5MzZiN2Q4NjU4MiJ9fX0=";

	private String id;
	private int weight;
	private int minAmount;
	private int maxAmount;

	public ExperienceCompostDrop(String id, int weight, int minAmount, int maxAmount) {
		this.id = id;
		this.weight = weight;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}

	@Override
	public void dropLoot(Block block) {
		int difference = maxAmount - minAmount;
		int amount = new Random().nextInt(difference + 1) + minAmount;
		ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 1, 0.5),
				EntityType.EXPERIENCE_ORB);
		orb.setExperience(amount);
	}

	@Override
	public void putInContainer(Block block) {
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
		ItemStack item = MoreCompost.getInstance().getWrapper().skullFromValue(EXPERIENCE_HEAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(MessageUtils.color("&6&l" + getId()));
		item.setItemMeta(meta);
		return item;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	@Override
	public String getType() {
		return "experience";
	}

}
