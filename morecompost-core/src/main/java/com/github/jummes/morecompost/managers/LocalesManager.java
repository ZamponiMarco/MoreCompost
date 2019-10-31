package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.locales.LocaleString;
import com.github.jummes.morecompost.settings.Settings;
import com.github.jummes.morecompost.utils.MessageUtils;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;

public class LocalesManager implements DataManager {

	private final static String FOLDERNAME = "locale";
	private final static List<String> DEFAULT_LOCALES = Lists.newArrayList("en-US.yml", "it-IT.yml");

	private MoreCompost plugin;

	private String filename;
	private File dataFile;
	private YamlConfiguration dataYaml;
	private Map<LocaleString, List<String>> locale = new EnumMap<LocaleString, List<String>>(LocaleString.class);

	public LocalesManager() {
		this.plugin = MoreCompost.getInstance();
		this.filename = plugin.getSettingsManager().getSetting(Settings.LOCALE) + ".yml";

		loadDataFile();
		loadDataYaml();
		loadData();
	}

	@Override
	public void loadDataFile() {
		File folder = new File(plugin.getDataFolder(), FOLDERNAME);

		if (!folder.exists()) {
			folder.mkdir();
		}

		DEFAULT_LOCALES.forEach(localeString -> {
			File localeFile = new File(folder, localeString);
			if (!localeFile.exists()) {
				plugin.saveResource(FOLDERNAME + File.separatorChar + localeString, false);
			}
		});

		dataFile = new File(folder, filename);
		if (!dataFile.exists()) {
			dataFile = new File(folder, "en-US.yml");
		}
	}

	@Override
	public void loadDataYaml() {
		dataYaml = YamlConfiguration.loadConfiguration(dataFile);
	}

	@Override
	public void loadData() {
		Arrays.stream(LocaleString.values()).forEach(localeString -> {
			locale.put(localeString,
					dataYaml.getStringList(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, localeString.name()))
							.stream().map(string -> MessageUtils.color(string)).collect(Collectors.toList()));
		});
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	@Override
	public void reloadData() {
		this.filename = plugin.getSettingsManager().getSetting(Settings.LOCALE) + ".yml";
		DataManager.super.reloadData();
	}

	public Map<LocaleString, List<String>> getLocaleMap() {
		return locale;
	}

	public List<String> getLocaleString(LocaleString localeString) {
		return locale.get(localeString);
	}

	public String getSingleLocaleString(LocaleString localeString) {
		return String.join(" ", locale.get(localeString));
	}

}
