package com.github.jummes.morecompost.compostabletables.factory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.compostables.Compostable;
import com.github.jummes.morecompost.compostables.DefaultCompostable;
import com.github.jummes.morecompost.compostabletables.CompostableTable;

public class CompostableTableFactory {

	private static int lastUsedPriority = 1000000;

	public static CompostableTable buildCompostableTable(Permission permission, ConfigurationSection section) {

		int priority = section.getInt("priority");
		if (priority == 0) {
			priority = lastUsedPriority++;
		}

		Set<Compostable> set = new HashSet<>();

		ConfigurationSection compostables = section.getConfigurationSection("compostables");

		compostables.getKeys(false).forEach(key -> {
			ConfigurationSection compostableSection = compostables.getConfigurationSection(key);
			Material material = Material.valueOf(compostableSection.getString("material", "DIRT").toUpperCase());
			int minRolls = compostableSection.getInt("minRolls", 1);
			int maxRolls = compostableSection.getInt("maxRolls", 1);
			double chance = compostableSection.getDouble("chance", 0.5);
			Optional<String> forcedDropTableId = Optional.ofNullable(compostableSection.getString("forcedDropTableId"));
			Compostable compostable = new Compostable(compostableSection.getName(), material, minRolls, maxRolls,
					chance, forcedDropTableId);
			set.add(compostable);
		});

		boolean replaceDefaultCompostables = section.getBoolean("replaceDefaultCompostables", false);
		if (!replaceDefaultCompostables)
			set.addAll(defaultCompostable(permission).getCompostables());

		return new CompostableTable(permission, priority, set, replaceDefaultCompostables, true);
	}

	public static CompostableTable defaultCompostable(Permission permission) {
		Set<Compostable> set = new HashSet<>();
		Arrays.stream(DefaultCompostable.values()).forEach(compostable -> set.add(compostable.getCompostable()));
		return new CompostableTable(permission, Integer.MAX_VALUE, set, false, false);
	}

}
