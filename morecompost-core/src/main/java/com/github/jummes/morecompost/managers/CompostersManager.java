package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.jummes.morecompost.core.MoreCompost;

public class CompostersManager implements DataManager {

	private final static String FOLDERNAME = "data";
	private final static String FILENAME = "composters.yml";

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;

	public CompostersManager() {
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
		dataYaml.getKeys(false).forEach(key -> {
			UUID uuid = UUID.fromString(key);
			List<String> list = dataYaml.getStringList(key);

			// Block validation
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext()) {
				try {
					Block block = getLocationFromString(iterator.next()).getBlock();
					if (!block.getType().equals(Material.COMPOSTER)) {
						iterator.remove();
					} else {
						block.setMetadata("owner", new FixedMetadataValue(plugin, uuid));
					}
				} catch (Exception e) {
					iterator.remove();
				}
			}

			dataYaml.set(key, list);
		});
		saveConfig();
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	/**
	 * Adds to the database the block given in input with p as owner
	 * 
	 * @param p owner of the block
	 * @param b block to add to database
	 */
	public void addBlockToPlayer(Player p, Block b) {
		String uuid = p.getUniqueId().toString();
		List<String> list = dataYaml.getStringList(uuid);
		list.add(getStringFromLocation(b.getLocation()));
		dataYaml.set(uuid, list);
		saveConfig();
	}

	/**
	 * Removes from the database the block given in input. It does nothing if block
	 * has no owner.
	 * 
	 * @param b block to remove
	 */
	public void removeBlockFromPlayer(Block b) {
		String uuid = null;
		try {
			uuid = (String) b.getMetadata("owner").stream()
					.filter(metadata -> metadata.getOwningPlugin() instanceof MoreCompost).findFirst().get().value()
					.toString();
		} catch (NoSuchElementException e) {
			return;
		}
		List<String> list = dataYaml.getStringList(uuid);
		list.remove(getStringFromLocation(b.getLocation()));
		dataYaml.set(uuid, list);
		saveConfig();
	}

	private String getStringFromLocation(Location location) {
		return String.format("%s:%d:%d:%d", location.getWorld().getName(), location.getBlockX(), location.getBlockY(),
				location.getBlockZ());
	}

	private Location getLocationFromString(String string) {
		String[] params = string.split(":");
		return new Location(Bukkit.getWorld(params[0]), Double.valueOf(params[1]), Double.valueOf(params[2]),
				Double.valueOf(params[3]));
	}

}
