package com.github.jummes.morecompost.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Handles plugin data
 * 
 * @author Marco
 *
 */
public interface DataManager {

	/**
	 * Loads the data file and sets it in an instance variable
	 */
	public void loadDataFile();

	/**
	 * Loads the yaml configuration, sets it in an instance variable
	 */
	public void loadDataYaml();

	/**
	 * Loads the data from the yaml configurations and puts it in his data structure
	 */
	public void loadData();

	/**
	 * Gets the yaml configuration
	 * 
	 * @return yaml configuration
	 */
	public YamlConfiguration getDataYaml();

	/**
	 * Gets the configuration file
	 * 
	 * @return configuration file
	 */
	public File getDataFile();

	/**
	 * Saves the configuration file
	 * 
	 */
	public default void saveConfig() {
		try {
			getDataYaml().save(getDataFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reloads data
	 * 
	 */
	public default void reloadData() {
		loadDataFile();
		loadDataYaml();
		loadData();
	}

}
