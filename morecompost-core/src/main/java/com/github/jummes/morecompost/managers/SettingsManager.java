package com.github.jummes.morecompost.managers;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.settings.Settings;

public class SettingsManager implements DataManager {

	private final static String FILENAME = "config.yml";
	private final static String CONFIG_VERSION = "1.0.1";

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private Map<Settings, String> settings = new EnumMap<Settings, String>(Settings.class);

	public SettingsManager() {
		this.plugin = MoreCompost.getInstance();

		loadDataFile();
		loadDataYaml();
		loadData();

		updateConfig();
	}

	@Override
	public void loadDataFile() {
		dataFile = new File(plugin.getDataFolder(), FILENAME);
		if (!dataFile.exists()) {
			plugin.saveDefaultConfig();
		}
	}

	@Override
	public void loadDataYaml() {
		dataYaml = (YamlConfiguration) plugin.getConfig();
	}

	@Override
	public void loadData() {
		settings.put(Settings.METRICS, dataYaml.getString("metrics", "true"));
		settings.put(Settings.UPDATE_CHECKER, dataYaml.getString("updateChecker", "true"));
		settings.put(Settings.VERSION, dataYaml.getString("version", ""));
		settings.put(Settings.LOCALE, dataYaml.getString("locale", "en-US"));
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	private void updateConfig() {
		if (!settings.get(Settings.VERSION).equals(CONFIG_VERSION)) {
			dataFile.delete();
			plugin.saveDefaultConfig();
		}
	}

	public void saveConfig() {
		try {
			dataYaml.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<Settings, String> getSettings() {
		return settings;
	}

	public String getSetting(Settings setting) {
		return settings.get(setting);
	}
}
