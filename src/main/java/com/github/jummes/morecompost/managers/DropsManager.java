package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.droptables.factory.DropTableFactory;

public class DropsManager implements DataManager {

	private final static String FILENAME = "drops.yml";
	private final static String PERM_PREFIX = "morecompost.drops.";
	private final static Permission DEFAULT_PERM = new Permission(PERM_PREFIX + "default", PermissionDefault.TRUE);

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private LinkedHashMap<Permission, DropTable> percentages;

	public DropsManager() {
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
		percentages = new LinkedHashMap<>();
		dataYaml.getKeys(false).forEach(key -> addDropTable(key));
		percentages.putIfAbsent(DEFAULT_PERM, DropTableFactory.defaultDropTable());
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	private void addDropTable(String key) {
		percentages.put(key.equalsIgnoreCase("default") ? DEFAULT_PERM : new Permission(PERM_PREFIX + key),
				DropTableFactory.buildDropTable(dataYaml.getConfigurationSection(key)));
	}

	public Map<Permission, DropTable> getPercentages() {
		return percentages;
	}

	public Permission getHighestPermission(Player player) {
		try {
			return percentages.keySet().stream().filter(permission -> player.hasPermission(permission)).findFirst()
					.orElse(DEFAULT_PERM);
		} catch (Exception e) {
			return DEFAULT_PERM;
		}
	}

}
