package de.rainix.Ban.commands;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import sqlapi.player.PlayerProfile;

public class BanCommand extends Command {

	public BanCommand() {
		super("ban");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("ban")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("setcomment")) {
					if (args.length > 2) {
						String UUID = Main.uuidFetcher(args[1]).toString();
						if (Main.banUtil.isBanned(UUID)) {
							String msg = args[2];
							for (int i = 3; i < args.length; i++) {
								msg += " " + args[i];
							}
							Main.banUtil.setComment(UUID, msg);
							sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Kommentar von §6"
									+ Main.nameFetcher(UUID) + "§a geändert!"));
						} else {
							sender.sendMessage(Main.toBase(Main.PREFIX + "§cDer Spieler §6" + Main.nameFetcher(UUID)
									+ "§c ist nicht gebannt!"));
						}
					} else {
						sender.sendMessage(Main
								.toBase(Main.PREFIX + "§cBitte benutze: §6/ban setComment <Spieler> <Kommentar>§c!"));
					}
				} else {
					if (args.length == 2) {
						String code = args[1];
						String uuid = Main.uuidFetcher(args[0]);
						String name = Main.nameFetcher(uuid);
						if(uuid != null) {
							if (code.equals("0")) {
								try {
									String ip = new PlayerProfile(uuid).getIP();
									if (ip.equalsIgnoreCase("ip")) {
										ip = "";
									}
									Main.banUtil.permbanPlayer(uuid, sender.getName(), "Hacking", "", ip);
								} catch (Exception e) {
									Main.banUtil.permbanPlayer(uuid, sender.getName(), "Hacking", "", "");
								}
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
										+ "§4§l PERMANENT§r§a von diesem Netzwerk wegen §6Hacking§a gebannt!"));
							} else if (code.equals("1")) {
								banPlayer(uuid, name, sender.getName(), "Bugusing",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
										+ "§4 ZEITLICH§r§a von diesem Netzwerk wegen §6Bugusing§a gebannt!"));
							} else if (code.equals("2")) {
								banPlayer(uuid, name, sender.getName(), "Teaming",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
										+ "§4 ZEITLICH§r§a von diesem Netzwerk wegen §6Teaming§a gebannt!"));
							} else if (code.equals("3")) {
								banPlayer(uuid, name, sender.getName(), "Random Killing",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
										+ "§4 ZEITLICH§r§a von diesem Netzwerk wegen §6Random Killing§a gebannt!"));
							} else if (code.equals("4")) {
								banPlayer(uuid, name, sender.getName(), "Trolling",
										System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5, false);
								sender.sendMessage(Main.toBase(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
										+ "§4 ZEITLICH§r§a von diesem Netzwerk wegen §6Trolling§a gebannt!"));
							} else {
								sender.sendMessage(
										Main.toBase(Main.PREFIX + "§cBitte benutze: §6/ban <Spieler> <Grundnummer>§c!"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§a§lGründe:"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c0 §7: §4Hacking, §4§lPermanent"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c1 §7: §4Bugusing, §65 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c2 §7: §4Teaming, §63 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c3 §7: §4Random Killing, §63 Tage"));
								sender.sendMessage(Main.toBase(Main.PREFIX + "§c4 §7: §4Trolling, §61 Tag"));
							}
						}else {
							sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler war noch nicht auf dem Netzwerk!"));
						}
					} else if (args.length == 1) {
						sender.sendMessage(
								Main.toBase(Main.PREFIX + "§cBitte benutze: §6/ban <Spieler> <Grundnummer>§c!"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§a§lGründe:"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c0 §7: §4Hacking, §4§lPermanent"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c1 §7: §4Bugusing, §65 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c2 §7: §4Teaming, §63 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c3 §7: §4Random Killing, §63 Tage"));
						sender.sendMessage(Main.toBase(Main.PREFIX + "§c4 §7: §4Trolling, §61 Tag"));
					}
				}
			} else {
				sender.sendMessage(Main.toBase(Main.PREFIX
						+ "§cBitte benutze: §6/ban <Spieler> <Grundnummer> §7/§6 /ban help§7/§6 /ban setComment <Spieler> <Kommentar>§c!"));
			}
		} else {
			sender.sendMessage(Main.toBase(Main.NO_PERMISSION));
		}
	}

	public void banPlayer(String uuid, String name, String executor, String reason, long l, boolean b) {
		try {
			String ip = new PlayerProfile(uuid).getIP();
			if (ip.equalsIgnoreCase("ip")) {
				ip = "";
			}
			Main.banUtil.banPlayer(uuid, executor, reason, l, "", ip, false);
		} catch (Exception e) {
			Main.banUtil.banPlayer(uuid, executor, reason, l, "", "", false);
		}
		if (ProxyServer.getInstance().getPlayer(name) != null) {
			ProxyServer.getInstance().getPlayer(name).disconnect(Main.toBase("ban temp"));
		}
	}

}
