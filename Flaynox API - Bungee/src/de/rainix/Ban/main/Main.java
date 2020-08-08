package de.rainix.Ban.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import de.rainix.Ban.Utils.BanUtil;
import de.rainix.Ban.Utils.MuteUtil;
import de.rainix.Ban.commands.BanCommand;
import de.rainix.Ban.commands.BanListCommand;
import de.rainix.Ban.commands.MuteCommand;
import de.rainix.Ban.commands.MuteListCommand;
import de.rainix.Ban.commands.PermBanCommand;
import de.rainix.Ban.commands.PermMuteCommand;
import de.rainix.Ban.commands.TempBanCommand;
import de.rainix.Ban.commands.TempMuteCommand;
import de.rainix.Ban.commands.UnbanCommand;
import de.rainix.Ban.commands.UnmuteCommand;
import de.rainix.Ban.listeners.ChatListener;
import de.rainix.Ban.listeners.JoinListener;
import de.rainix.Ban.mysql.Cache;
import de.rainix.Ban.mysql.MySQL;
import flaynoxapi.message.MessageManager;
import flaynoxapi.message.Placeholder;
import flaynoxapi.utils.PluginConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import sqlapi.player.PlayerProfile;

public class Main extends Plugin {

	public static Main plugin;

	public static final String PREFIX = "§7[§cBan§7] §r";
	public static final String NO_PERMISSION = "§cDazu hast du keine Rechte!";
	public static final String OFFLINE_PLAYER = "§cDer Spieler ist nicht online!";
	public static final String BE_A_PLAYER = "§cDiesen Befehl kannst du nur als Spieler ausführen!";
	public static BanUtil banUtil;
	public static MuteUtil muteUtil;
	public static PluginConfig pluginConfig;

	public void onEnable() {
		plugin = this;
		PluginManager pm = getProxy().getPluginManager();
		MessageManager.setupMessageManager(this);
		register(pm);
		MySQL.connect();
		getProxy().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {
				for (ProxiedPlayer all : getProxy().getPlayers()) {
					if (banUtil.isPermBanned(all.getUniqueId().toString())) {
						all.disconnect(Placeholder.replaceTimeAndReason(
								new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_PERM), "", "",
								banUtil.getCause(all.getUniqueId().toString())));
					}
					if (banUtil.isTempBanned(all.getUniqueId().toString())) {
						long time = Main.banUtil.getLeftTime(all.getUniqueId().toString());
						SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
						String time1 = sdf1.format(time);
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						String time2 = sdf2.format(time);
						all.disconnect(Placeholder.replaceTimeAndReason(
								new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_TEMP), time1, time2,
								banUtil.getCause(all.getUniqueId().toString())));
					}
				}
			}
		}, 20, 20, TimeUnit.SECONDS);
		banUtil = new BanUtil();
		muteUtil = new MuteUtil();
		PreparedStatement ps1;
		PreparedStatement ps2;
		try {
			ps1 = MySQL.con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Ban (UUID VARCHAR(100), EXECUTOR VARCHAR(100), SINCE LONG, UNTIL LONG, CAUSE VARCHAR(100), COMMENT VARCHAR(100), IP VARCHAR(100), IPBAN BOOLEAN)");
			ps1.executeUpdate();
			ps1.close();
			ps2 = MySQL.con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Mute (UUID VARCHAR(100), EXECUTOR VARCHAR(100), SINCE LONG, UNTIL LONG, CAUSE VARCHAR(100), COMMENT VARCHAR(100), IP VARCHAR(100), IPBAN BOOLEAN)");
			ps2.executeUpdate();
			ps2.close();
		} catch (Exception e) {
			System.out.println(Cache.ANSI_RED + "[MySQL] Es konnte nichts von der Datenbank geladen werden! {Ban}"
					+ Cache.ANSI_WHITE);
			System.out.println(e.getMessage());
		}
		pluginConfig = new PluginConfig();
		initConfig();
	}

	private void initConfig() {
		pluginConfig.addDefault("languages.German", "lang_german.yml");
        pluginConfig.addDefault("languages.English", "lang_english.yml");

        pluginConfig.setDefaultConfig();
	}

	private void register(PluginManager pm) {
		pm.registerListener(this, new JoinListener());
		pm.registerListener(this, new ChatListener());
		pm.registerCommand(this, new BanCommand());
		pm.registerCommand(this, new UnbanCommand());
		pm.registerCommand(this, new BanListCommand());
		pm.registerCommand(this, new PermBanCommand());
		pm.registerCommand(this, new TempBanCommand());
		pm.registerCommand(this, new MuteCommand());
		pm.registerCommand(this, new UnmuteCommand());
		pm.registerCommand(this, new PermMuteCommand());
		pm.registerCommand(this, new TempMuteCommand());
		pm.registerCommand(this, new MuteListCommand());
	}

	public static BaseComponent[] toBase(String text) {
		return new ComponentBuilder(text).create();
	}

	public static String uuidFetcher(String name) {
//		try {
//			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
//			InputStreamReader r = new InputStreamReader(url.openStream());
//			return new JsonParser().parse(r).getAsJsonObject().get("id").getAsString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		try {
			return PlayerProfile.getUUIDFromName(name);
		} catch (SQLException e) {
			return null;
		}
	}

	public static String nameFetcher(String uuid) {
//		try {
//			if (uuid.contains("-")) {
//				uuid = uuid.replaceAll("-", "");
//			}
//			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
//			InputStreamReader r = new InputStreamReader(url.openStream());
//			return new JsonParser().parse(r).getAsJsonObject().get("name").getAsString();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		try {
			return new PlayerProfile(uuid).getName();
		} catch (SQLException e) {
			return null;
		}
	}
}
