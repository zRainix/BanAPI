package flaynoxapi.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfig {

	private File file;
	private Configuration config;

	private HashMap<String, Object> defaults;

	public PluginConfig() {

		try {

			File f = new File(Main.plugin.getDataFolder().getPath());

			if (!f.exists())
				f.mkdir();

			this.file = new File(Main.plugin.getDataFolder(), "config.yml");

			if (!file.exists())
				file.createNewFile();

			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

		} catch (IOException e) {
			e.printStackTrace();
		}

		defaults = new HashMap<String, Object>();

	}

	public Configuration getConfig() {

		return config;
	}

	public void addDefault(String path, Object value) {

		if (!defaults.containsKey(path)) {

			defaults.put(path, value);

		}

	}

	public void setDefaultConfig() {

		boolean changed = false;

		for (String path : defaults.keySet()) {

			if (config.get(path) == null) {

				config.set(path, defaults.get(path));

				changed = true;

			}

		}

		if (changed)
			saveConfig();

	}

	public void reloadConfig() {

		try {

			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public void saveConfig() {

		try {

			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

}