package flaynoxapi.message;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import sqlapi.player.PlayerLanguage;
import sqlapi.sql.SQLManager;

public class MessageManager {

	private static Plugin instance;

	private static HashMap<Language, HashMap<Message, String>> messageMapForLanguage = new HashMap<Language, HashMap<Message, String>>();
	private static HashMap<UUID, HashMap<Message, String>> messageMapForUUID = new HashMap<UUID, HashMap<Message, String>>();

	private HashMap<Message, String> messages;

	public MessageManager(ProxiedPlayer player) {

		this(player.getUniqueId());

	}

	public MessageManager(UUID uuid) {

		if (messageMapForUUID.containsKey(uuid)) {

			messages = messageMapForUUID.get(uuid);

		} else {

			Language language = getLanguageFromUUID(uuid);

			if (language != null) {

				HashMap<Message, String> messageMap = messageMapForLanguage.get(language);

				messageMapForUUID.put(uuid, messageMap);
				messages = messageMap;

			} else {

				try {
					throw new MessageExeption(
							"Cannot find any languages. Check the config and the message folder. Then reload the plugin.");
				} catch (MessageExeption e) {
					e.printStackTrace();
				}

			}
		}

	}

	public MessageManager(Language language) {

		if (messageMapForLanguage.containsKey(language)) {

			messages = messageMapForLanguage.get(language);

		} else {

			try {
				throw new MessageExeption("This language does not exist. (" + language.getName() + ")");
			} catch (MessageExeption e) {
				e.printStackTrace();
			}

		}

	}

	public String getMessage(Message message) {

		if (messages.containsKey(message))
			return messages.get(message);
		return "N/A";

	}

	private Language getLanguageFromUUID(UUID uuid) {

		if (SQLManager.isConnected()) {

			try {

				PlayerLanguage playerLanguage = new PlayerLanguage(uuid);

				String languageName = playerLanguage.getLanguage();

				for (Language current : messageMapForLanguage.keySet()) {

					if (current.getName().equalsIgnoreCase(languageName))
						return current;

				}

			} catch (Exception ex) {

				ex.printStackTrace();

			}

		}

		if (!messageMapForLanguage.isEmpty())
			return (Language) messageMapForLanguage.keySet().toArray()[0];

		return null;

	}

	public static void setupMessageManager(Plugin instance) {

		MessageManager.instance = instance;

		File configFile = new File(instance.getDataFolder(), "config.yml");

		if (configFile.exists()) {

			try {
				Language.initLanguages(ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile));
				initFiles();
				initLanguageMessages();

			} catch (Exception ex) {

				ex.printStackTrace();

			}

		}

	}

	private static void initFiles() {

		try {

			for (Language current : Language.languages) {

				File file = new File(instance.getDataFolder() + "/message", current.getFileName());

				createNewFile(file);

				Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

				initFile(file, configuration);

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private static void initFile(File file, Configuration configuration) {

		boolean updated = false;

		for (Message current : Message.values()) {

			if (configuration.get(current.getMessagePath()) == null) {

				configuration.set(current.getMessagePath(), current.getStandardText());
				updated = true;

			}

		}

		if (updated)
			saveFile(configuration, file);

	}

	private static void initLanguageMessages() {

		try {

			for (Language current : Language.languages) {

				File file = new File(instance.getDataFolder() + "/message", current.getFileName());
				Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

				HashMap<Message, String> messageMap = new HashMap<Message, String>();

				for (Message messages : Message.values()) {

					String path = messages.getMessagePath();

					if (configuration.get(path) != null) {

						if (!messageMap.containsKey(messages))
							messageMap.put(messages, configuration.getString(path).replaceAll("&", "§"));

					}

				}

				messageMapForLanguage.put(current, messageMap);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private static void createNewFile(File file) {

		try {

			File directory = new File(file.getParent());

			if (!directory.exists())
				directory.mkdir();

			if (!file.exists())
				file.createNewFile();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	private static void saveFile(Configuration configuration, File file) {

		try {

			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
