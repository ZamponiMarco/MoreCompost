package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.locale.LocaleString;
import com.github.jummes.morecompost.utils.MessageUtils;

public class LocalesManager implements DataManager {

	private final static String FOLDERNAME = "locale";
	private final static String FILENAME = "locale.yml";

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private Map<LocaleString, List<String>> locale = new EnumMap<LocaleString, List<String>>(LocaleString.class);

	public LocalesManager() {
		this.plugin = MoreCompost.getInstance();

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

		dataFile = new File(folder, FILENAME);
		if (!dataFile.exists()) {
			plugin.saveResource(FOLDERNAME + File.separatorChar + FILENAME, false);
		}
	}

	@Override
	public void loadDataYaml() {
		dataYaml = YamlConfiguration.loadConfiguration(dataFile);
	}

	@Override
	public void loadData() {
		locale.put(LocaleString.TYPE_DESCRIPTION, dataYaml.getStringList("typeDescription"));
		locale.put(LocaleString.WEIGHT_DESCRIPTION, dataYaml.getStringList("weightDescription"));
		locale.put(LocaleString.MIN_AMOUNT_DESCRIPTION, dataYaml.getStringList("minAmountDescription"));
		locale.put(LocaleString.MAX_AMOUNT_DESCRIPTION, dataYaml.getStringList("maxAmountDescription"));
		locale.put(LocaleString.MIN_COUNT_DESCRIPTION, dataYaml.getStringList("minCountDescription"));
		locale.put(LocaleString.MAX_COUNT_DESCRIPTION, dataYaml.getStringList("maxCountDescription"));
		locale.put(LocaleString.DISPLAY_NAME_DESCRIPTION, dataYaml.getStringList("displayNameDescription"));
		locale.put(LocaleString.TEXTURE_DESCRIPTION, dataYaml.getStringList("textureDescription"));
		locale.put(LocaleString.CHANCE_DESCRIPTION, dataYaml.getStringList("chanceDescription"));
		locale.put(LocaleString.MATERIAL_DESCRIPTION, dataYaml.getStringList("materialDescription"));
		locale.put(LocaleString.FORCED_DROP_TABLE_ID_DESCRIPTION, dataYaml.getStringList("forcedDropTableIdDescription"));
		locale.put(LocaleString.MIN_ROLLS_DESCRIPTION, dataYaml.getStringList("minRollsDescription"));
		locale.put(LocaleString.MAX_ROLLS_DESCRIPTION, dataYaml.getStringList("maxRollsDescription"));
		locale.put(LocaleString.REPLACE_DEFAULT_COMPOSTABLES_DESCRIPTION, dataYaml.getStringList("replaceDefaultCompostablesDescription"));
		locale.put(LocaleString.PRIORITY_DESCRIPTION, dataYaml.getStringList("priorityDescription"));
		locale.put(LocaleString.DROPS_LIST_DESCRIPTION, dataYaml.getStringList("dropsListDescription"));
		locale.put(LocaleString.COMPOSTABLES_LIST_DESCRIPTION, dataYaml.getStringList("compostablesListDescription"));
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	public Map<LocaleString, List<String>> getLocaleMap() {
		return locale;
	}

	public List<String> getLocaleString(LocaleString localeString) {
		return locale.get(localeString).stream().map(string -> MessageUtils.color(string)).collect(Collectors.toList());
	}

}
