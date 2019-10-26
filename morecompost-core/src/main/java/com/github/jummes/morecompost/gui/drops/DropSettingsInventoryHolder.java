package com.github.jummes.morecompost.gui.drops;

import org.bukkit.inventory.InventoryHolder;

import com.github.jummes.morecompost.gui.MoreCompostInventoryHolder;

public abstract class DropSettingsInventoryHolder extends MoreCompostInventoryHolder {

	protected String dropTableId;
	protected String dropId;
	protected InventoryHolder holder;


	public DropSettingsInventoryHolder(String dropTableId, String dropId, InventoryHolder holder) {
		this.dropTableId = dropTableId;
		this.dropId = dropId;
		this.holder = holder;
	}

	public String getDropTableId() {
		return dropTableId;
	}

	public String getDropId() {
		return dropId;
	}
	
	public InventoryHolder getHolder() {
		return holder;
	}

}
