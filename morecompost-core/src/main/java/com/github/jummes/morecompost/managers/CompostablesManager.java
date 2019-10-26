package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.github.jummes.morecompost.compostabletables.CompostableTable;
import com.github.jummes.morecompost.compostabletables.factory.CompostableTableFactory;
import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.utils.MessageUtils;

public class CompostablesManager implements DataManager {

	private final static String FILENAME = "compostables.yml";
	private final static String PERM_PREFIX = "morecompost.compostables.";
	private final static Permission DEFAULT_PERM = new Permission(PERM_PREFIX + "default", PermissionDefault.TRUE);
	private final static String ERROR_MSG = MessageUtils.color(
			"&cAn error has occurred while loading &6Compostables&c, please check your &6compostables.yml&c file.");

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private LinkedHashMap<Permission, CompostableTable> compostables;

	public CompostablesManager() {
		this.plugin = MoreCompost.getInstance();

		loadDataFile();
		loadDataYaml();
		loadData();
	}

	@Override
	public void loadDataFile() {
		dataFile = new File(plugin.getDataFolder(), FILENAME);
		if (!dataFile.exists()) {
			plugin.saveResource(FILENAME, false);
		}
	}

	@Override
	public void loadDataYaml() {
		dataYaml = YamlConfiguration.loadConfiguration(dataFile);
	}

	@Override
	public void loadData() {
		compostables = new LinkedHashMap<Permission, CompostableTable>();
		try {
			dataYaml.getKeys(false).forEach(this::addCompostable);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().warning(ERROR_MSG);
		}
		compostables.putIfAbsent(DEFAULT_PERM, CompostableTableFactory.defaultCompostable(DEFAULT_PERM));

		compostables = compostables.entrySet().stream()
				.sorted((entry1, entry2) -> entry1.getValue().getPriority() - entry2.getValue().getPriority())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
	}

	public Map<Permission, CompostableTable> getCompostables() {
		return compostables;
	}

	private void addCompostable(String key) {
		Permission permission = key.equalsIgnoreCase("default") ? DEFAULT_PERM : new Permission(PERM_PREFIX + key);
		compostables.put(permission,
				CompostableTableFactory.buildCompostableTable(permission, dataYaml.getConfigurationSection(key)));
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	public Permission getHighestPermission(Player player) {
		try {
			return compostables.keySet().stream().filter(permission -> player.hasPermission(permission)).findFirst()
					.orElse(DEFAULT_PERM);
		} catch (Exception e) {
			return DEFAULT_PERM;
		}
	}

	public CompostableTable get(String compostableTableId) {
		return compostables.values().stream()
				.filter(compostableTable -> compostableTable.getId().equals(compostableTableId)).findFirst().get();
	}

	public void getDefaultCompostableTable(String name) {
		ConfigurationSection section = getDataYaml().createSection(name);
		section.set("compostables", section.createSection("compostables"));
	}

	public void getDefaultCompostable(String compostableTableId, String name) {
		getDataYaml().getConfigurationSection(compostableTableId).getConfigurationSection("compostables")
				.createSection(name);
	}

}
