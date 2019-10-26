package com.github.jummes.morecompost.compostabletables;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.compostables.Compostable;

public class CompostableTable {

	private static final String PERM_PREFIX = "morecompost.compostables.";

	private Permission permission;
	private int priority;
	private Set<Compostable> compostables;
	private boolean replaceDefaultCompostables;
	private boolean presentInConfig;

	public CompostableTable(Permission permission, int priority, Set<Compostable> compostables,
			boolean replaceDefaultCompostables, boolean presentInConfig) {
		this.permission = permission;
		this.priority = priority;
		this.compostables = compostables;
		this.replaceDefaultCompostables = replaceDefaultCompostables;
		this.presentInConfig = presentInConfig;
	}

	/**
	 * Checks if the material is present in the compostable set, if it is, tries to
	 * compost the block
	 * 
	 * @param block    block to compost
	 * @param material compostable material
	 * @return true if the compostable is contained inside the compostable table
	 */
	public boolean compost(Block block, Material material) {
		AtomicBoolean isPresent = new AtomicBoolean(false);
		compostables.stream().filter(compostable -> compostable.getMaterial().equals(material)).findFirst()
				.ifPresent(compostable -> {
					compostable.compost(block);
					isPresent.set(true);
				});
		return isPresent.get();
	}

	public String getId() {
		return permission.getName().substring(PERM_PREFIX.length(), permission.getName().length());
	}

	public Compostable get(String compostableId) {
		return compostables.stream().filter(compostable -> compostable.getId().equals(compostableId)).findFirst().get();
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Set<Compostable> getCompostables() {
		return compostables;
	}

	public boolean getReplaceDefaultCompostables() {
		return replaceDefaultCompostables;
	}

	public boolean isPresentInConfig() {
		return presentInConfig;
	}

}
