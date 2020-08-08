package de.rainix.Ban.commands;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import sqlapi.player.PlayerProfile;

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("mute")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("setcomment")) {
					if (args.length > 2) {
						String UUID = Main.uuidFetcher(args[1]).toString();
						if (Main.muteUtil.isMuted(UUID)) {
							String msg = args[2];
							for (int i = 3; i < args.length; i++) {
								msg += " " + args[i];
							}
							Main.muteUtil.setComment(UUID, msg);
							sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Kommentar von §6"
									+ Main.nameFetcher(UUID) + "§a geändert!"));
						} else {
							sender.sendMessage(Main.toBase(Main.PREFIX + "§cDer Spieler §6" + Main.nameFetcher(UUID)
									+ "§c ist nicht gemutet!"));
						}
					} else {
						sender.sendMessage(Main
								.toBase(Main.PREFIX + "§cBitte benutze: §6/mute setComment <Spieler> <Kommentar>§c!"));
					}
				} else {
					if (args.length == 2) {
						String code = args[1];
						String uuid = Main.uuidFetcher(args[0]);
						String name = Main.nameFetcher(uuid);
						if (uuid != null) {
							if (code.equals("0")) {
								try {
									String ip = new PlayerProfile(Main.uuidFetcher(args[0])).getIP();
									if (ip.equalsIgnoreCase("ip")) {
										ip = "";
									}
									Main.muteUtil.permmutePlayer(uuid, sender.getName(), "Extrem", "", ip);
								} catch (Exception e) {
									Main.muteUtil.permmutePlayer(uuid, sender.getName(), "Extrem", "", "");
								}
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4§l PERMANENT§r§a wegen §6Extrem§a gemutet!"));
							} else if (code.equals("1")) {
								mutePlayer(uuid, name, sender.getName(), "Morddrohung",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 31, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4 ZEITLICH§r§a wegen §6Morddrohung§a gemutet!"));
							} else if (code.equals("2")) {
								mutePlayer(uuid, name, sender.getName(), "Beleidigung (Schwer)",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 20, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4 ZEITLICH§r§a wegen §6Beleidigung (Schwer)§a gemutet!"));
							} else if (code.equals("3")) {
								mutePlayer(uuid, name, sender.getName(), "Beleidigung",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 10, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4 ZEITLICH§r§a wegen §6Beleidigung§a gemutet!"));
							} else if (code.equals("4")) {
								mutePlayer(uuid, name, sender.getName(), "Unangemessen",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4 ZEITLICH§r§a wegen §6Unangemessen§a gemutet!"));
							} else if (code.equals("5")) {
								mutePlayer(uuid, name, sender.getName(), "Werbung",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 1, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6"
										+ name + "§4 ZEITLICH§r§a wegen §6Werbung§a gemutet!"));
							} else {
								sender.sendMessage(Main
										.toBase(Main.PREFIX + "§cBitte benutze: §6/mute <Spieler> <Grundnummer>§c!"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§a§lGründe:"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c0 §7: §4Extrem, §6Permanent"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c1 §7: §4Morddrohung, §61 Monat"));
								sender.sendMessage(
										Main.toBase(Main.PREFIX + "§c2 §7: §4Beleidigung (Schwer), §620 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c3 §7: §4Beleidigung, §610 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c4 §7: §4Unangemessen, §63 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c5 §7: §4Werbung, §61 Tag"));
							}
						} else {
							sender.sendMessage(
									Main.toBase(Main.PREFIX + "§cDieser Spieler war noch nicht auf dem Netzwerk!"));
						}
					} else if (args.length == 1) {
						sender.sendMessage(
								Main.toBase(Main.PREFIX + "§cBitte benutze: §6/mute <Spieler> <Grundnummer>§c!"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§a§lGründe:"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c0 §7: §4Extrem, §6Permanent"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c1 §7: §4Morddrohung, §61 Monat"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c2 §7: §4Beleidigung (Schwer), §620 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c3 §7: §4Beleidigung, §610 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c4 §7: §4Unangemessen, §63 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c5 §7: §4Werbung, §61 Tag"));
					}
				}
			} else {
				sender.sendMessage(Main.toBase(Main.PREFIX
						+ "§cBitte benutze: §6/mute <Spieler> <Grundnummer> §7/§6 /mute help§7/§6 /mute setComment <Spieler> <Kommentar>§c!"));
			}
		} else

		{
			sender.sendMessage(Main.toBase(Main.NO_PERMISSION));
		}
	}

	public void mutePlayer(String uuid, String name, String executor, String reason, long l, boolean b) {
		try {
			String ip = new PlayerProfile(uuid).getIP();
			if (ip.equalsIgnoreCase("ip")) {
				ip = "";
			}
			Main.muteUtil.mutePlayer(uuid, executor, reason, l, "", ip, false);
		} catch (Exception e) {
			Main.muteUtil.mutePlayer(uuid, executor, reason, l, "", "", false);
		}
		if (ProxyServer.getInstance().getPlayer(name) != null) {
			ProxyServer.getInstance().getPlayer(name).disconnect(Main.toBase("mute temp"));
		}
	}

}
