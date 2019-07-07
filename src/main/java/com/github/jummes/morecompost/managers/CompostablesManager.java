package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.github.jummes.morecompost.compostables.Compostable;
import com.github.jummes.morecompost.compostables.factory.CompostableFactory;
import com.github.jummes.morecompost.core.MoreCompost;

public class CompostablesManager implements DataManager {

	private final static String FILENAME = "compostables.yml";
	private final static String PERM_PREFIX = "morecompost.composters.";
	private final static Permission DEFAULT_PERM = new Permission(PERM_PREFIX + "default", PermissionDefault.TRUE);

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private LinkedHashMap<Permission, Set<Compostable>> compostables;

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
		compostables = new LinkedHashMap<Permission, Set<Compostable>>();
		dataYaml.getKeys(false).forEach(this::addCompostable);
		compostables.putIfAbsent(DEFAULT_PERM, CompostableFactory.defaultCompostable());
	}

	public Map<Permission, Set<Compostable>> getCompostables() {
		return compostables;
	}

	private void addCompostable(String key) {
		compostables.put(key.equalsIgnoreCase("default") ? DEFAULT_PERM : new Permission(PERM_PREFIX + key),
				CompostableFactory.buildCompostable(dataYaml.getConfigurationSection(key)));
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

}
