package com.github.jummes.morecompost.drops.factory;

import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.ConfigurationSection;

import com.github.jummes.morecompost.drops.AbstractCompostDrop;

public interface CompostDropFactory {

	/**
	 * Builds a drop from a configuration section
	 * 
	 * @param drop
	 * @return
	 */
	public AbstractCompostDrop buildCompostDrop(ConfigurationSection drop);

	/**
	 * Gets the correct CompostDropFactory from a string 
	 * 
	 * @param type type of the CompostDrop
	 * @return the correct CompostDropFactory
	 */
	public static CompostDropFactory getCompostDropFactory(String type) {
		try {
			return (CompostDropFactory) Class.forName(
					"com.github.jummes.morecompost.drops.factory." + WordUtils.capitalize(type) + "CompostDropFactory")
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
