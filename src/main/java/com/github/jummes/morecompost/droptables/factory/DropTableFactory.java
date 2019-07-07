package com.github.jummes.morecompost.droptables.factory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.ItemCompostDrop;
import com.github.jummes.morecompost.drops.factory.CompostDropFactory;
import com.github.jummes.morecompost.droptables.DropTable;

public class DropTableFactory {

	public static DropTable buildDropTable(ConfigurationSection dropTable) {

		int minRolls = dropTable.getInt("minRolls", 1);

		int maxRolls = dropTable.getInt("maxRolls", 1);

		Map<Integer, CompostDrop> weightMap = new HashMap<>();

		int lastWeight = 0;
		for (String key : dropTable.getConfigurationSection("drops").getKeys(false)) {
			ConfigurationSection dropSection = dropTable.getConfigurationSection("drops." + key);
			lastWeight += dropSection.getInt("weight", 1);
			CompostDrop drop = CompostDropFactory.getCompostDropFactory(dropSection.getString("type", "item"))
					.buildCompostDrop(dropSection);
			weightMap.put(lastWeight, drop);
		}

		return new DropTable(minRolls, maxRolls, weightMap);
	}

	public static DropTable defaultDropTable() {
		Map<Integer, CompostDrop> weightMap = new HashMap<>();
		weightMap.put(1, new ItemCompostDrop(new ItemStack(Material.BONE_MEAL)));
		return new DropTable(1, 1, weightMap);
	}

}
