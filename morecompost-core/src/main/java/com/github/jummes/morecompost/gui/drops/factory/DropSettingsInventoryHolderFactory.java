package com.github.jummes.morecompost.gui.drops.factory;

import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.ExperienceCompostDrop;
import com.github.jummes.morecompost.drops.HeadCompostDrop;
import com.github.jummes.morecompost.drops.ItemCompostDrop;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.gui.drops.DropSettingsInventoryHolder;
import com.github.jummes.morecompost.gui.drops.ExperienceDropSettingsInventoryHolder;
import com.github.jummes.morecompost.gui.drops.HeadDropSettingsInventoryHolder;
import com.github.jummes.morecompost.gui.drops.ItemDropSettingsInventoryHolder;

public class DropSettingsInventoryHolderFactory {

	public static DropSettingsInventoryHolder buildDropSettingInventoryHolder(InventoryHolder holder, DropTable dropTable, CompostDrop drop) {
		if(drop instanceof ItemCompostDrop) {
			return new ItemDropSettingsInventoryHolder(dropTable.getId(), drop.getId(), holder);
		} else if(drop instanceof ExperienceCompostDrop) {
			return new ExperienceDropSettingsInventoryHolder(dropTable.getId(), drop.getId(), holder);
		} else if(drop instanceof HeadCompostDrop) {
			return new HeadDropSettingsInventoryHolder(dropTable.getId(), drop.getId(), holder);
		}
		return null;
	}
	
}
