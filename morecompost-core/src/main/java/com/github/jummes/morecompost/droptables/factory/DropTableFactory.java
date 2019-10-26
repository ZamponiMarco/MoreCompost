package com.github.jummes.morecompost.droptables.factory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.ItemCompostDrop;
import com.github.jummes.morecompost.drops.factory.CompostDropFactory;
import com.github.jummes.morecompost.droptables.DropTable;

public class DropTableFactory {

	private static int lastUsedPriority = 1000000;

	public static DropTable buildDropTable(Permission permission, ConfigurationSection section) {

		int minRolls = section.getInt("minRolls", 1);

		int maxRolls = section.getInt("maxRolls", 1);

		int priority = section.getInt("priority");
		if (priority == 0) {
			priority = lastUsedPriority++;
		}

		Map<Integer, CompostDrop> weightMap = new HashMap<>();

		int lastWeight = 0;
		for (String key : section.getConfigurationSection("drops").getKeys(false)) {
			ConfigurationSection dropSection = section.getConfigurationSection("drops." + key);
			lastWeight += dropSection.getInt("weight", 1);
			CompostDrop drop = CompostDropFactory.getCompostDropFactory(dropSection.getString("type", "item"))
					.buildCompostDrop(dropSection);
			weightMap.put(lastWeight, drop);
		}

		return new DropTable(permission, minRolls, maxRolls, priority, weightMap, true);
	}

	public static DropTable defaultDropTable(Permission permission) {
		Map<Integer, CompostDrop> weightMap = new HashMap<>();
		weightMap.put(1, new ItemCompostDrop("1", 1, new ItemStack(Material.BONE_MEAL), 1, 1));
		return new DropTable(permission, 1, 1, Integer.MAX_VALUE, weightMap, false);
	}

}
