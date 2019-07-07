package com.github.jummes.morecompost.compostables.factory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.github.jummes.morecompost.compostables.Compostable;
import com.github.jummes.morecompost.compostables.DefaultCompostable;

public class CompostableFactory {

	@SuppressWarnings("unused")
	public static Set<Compostable> buildCompostable(ConfigurationSection section) {
		Set<Compostable> set = new HashSet<>();

		section.getKeys(false).stream().filter(key -> !key.equalsIgnoreCase("replaceDefaultCompostables"))
				.forEach(key -> {
					ConfigurationSection compostableSection = section.getConfigurationSection(key);
					Material material = Material.valueOf(compostableSection.getString("material").toUpperCase());
					int minRolls = compostableSection.getInt("minRolls", 1);
					int maxRolls = compostableSection.getInt("maxRolls", 1);
					double chance = compostableSection.getDouble("chance", 0.5);
					Compostable compostable = new Compostable(material, minRolls, maxRolls, chance);
					set.add(compostable);
				});

		boolean b = section.getBoolean("replaceDefaultCompostables", false) ? false : set.addAll(defaultCompostable());

		return set;
	}

	public static Set<Compostable> defaultCompostable() {
		Set<Compostable> set = new HashSet<>();
		Arrays.stream(DefaultCompostable.values()).forEach(compostable -> set.add(compostable.getCompostable()));
		return set;
	}

}
