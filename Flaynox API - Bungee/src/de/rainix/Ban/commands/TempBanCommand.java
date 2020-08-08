package de.rainix.Ban.commands;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import sqlapi.player.PlayerProfile;

public class TempBanCommand extends Command {

	public TempBanCommand() {
		super("tempban");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("ban")) {
			if (args.length >= 3) {
				ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
				String ip = "";
				if (p != null) {
					try {
						ip += new PlayerProfile(p.getUniqueId()).getIP();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ip.equalsIgnoreCase("ip")) {
						ip = "";
					}
				}
				String UUID = Main.uuidFetcher(args[0]);
				if (UUID != null) {
					String name = Main.nameFetcher(UUID);
					String CAUSE = "";
					for (int i = 2; i < args.length; i++) {
						CAUSE = CAUSE + args[i] + " ";
					}
					long TIME = System.currentTimeMillis();
					if (args[1].endsWith("min")) {
						String[] t = args[1].split("min");
						try {
							long time = Integer.parseInt(t[0]);
							time = time * 1000 * 60;
							TIME = TIME + time;
							Main.banUtil.tempbanPlayer(UUID, sender.getName(), CAUSE, TIME, "", ip, false);
							sender.sendMessage(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
									+ " §awegen §6" + CAUSE + "§a für §6" + t[0] + " Minute(n) §agebannt!");
						} catch (NumberFormatException e) {
						}
					} else if (args[1].endsWith("h")) {
						String[] t = args[1].split("h");
						try {
							long time = Integer.parseInt(t[0]);
							time = time * 1000 * 60 * 60;
							TIME = TIME + time;
							Main.banUtil.tempbanPlayer(UUID, sender.getName(), CAUSE, TIME, "", ip, false);
							sender.sendMessage(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
									+ " §awegen §6" + CAUSE + "§a für §6" + t[0] + " Stunde(n) §agebannt!");
						} catch (NumberFormatException e) {
						}
					} else if (args[1].endsWith("d")) {
						String[] t = args[1].split("d");
						try {
							long time = Integer.parseInt(t[0]);
							time = time * 1000 * 60 * 60 * 24;
							TIME = TIME + time;
							Main.banUtil.tempbanPlayer(UUID, sender.getName(), CAUSE, TIME, "", ip, false);
							sender.sendMessage(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
									+ " §awegen §6" + CAUSE + "§a für §6" + t[0] + " Tag(e) §agebannt!");
						} catch (NumberFormatException e) {
						}
					} else if (args[1].endsWith("w")) {
						String[] t = args[1].split("w");
						try {
							long time = Integer.parseInt(t[0]);
							time = time * 1000 * 60 * 60 * 24 * 7;
							TIME = TIME + time;
							Main.banUtil.tempbanPlayer(UUID, sender.getName(), CAUSE, TIME, "", ip, false);
							sender.sendMessage(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
									+ " §awegen §6" + CAUSE + "§a für §6" + t[0] + " Woche(n) §agebannt!");
						} catch (NumberFormatException e) {
						}
					} else if (args[1].endsWith("m")) {
						String[] t = args[1].split("m");
						try {
							long time = Integer.parseInt(t[0]);
							time = time * 1000 * 60 * 60 * 24 * 30;
							TIME = TIME + time;
							Main.banUtil.tempbanPlayer(UUID, sender.getName(), CAUSE, TIME, "", ip, false);
							sender.sendMessage(Main.PREFIX + "§aDu hast erfolgreich den Spieler §6" + name
									+ " §awegen §6" + CAUSE + "§a für §6" + t[0] + " Monat(e) §agebannt!");
						} catch (NumberFormatException e) {
						}
					} else {
						sender.sendMessage(Main.PREFIX + "§cSyntax: §6<Zeit>+");
						sender.sendMessage("§6<Zeit>min : §7Zeit in Minuten");
						sender.sendMessage("§6<Zeit>h : §7Zeit in Stunden");
						sender.sendMessage("§6<Zeit>d : §7Zeit in Tagen");
						sender.sendMessage("§6<Zeit>w : §7Zeit in Wochen");
						sender.sendMessage("§6<Zeit>m : §7Zeit in Monaten");
						return;
					}
					try {
						sender.sendMessage("§4" + name + "§7:");
						long time = Main.banUtil.getLeftTime(UUID);
						SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM");
						String time1 = sdf1.format(time);
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						String time2 = sdf2.format(time);
						sender.sendMessage("   §7Gebannt bis: §6" + time1 + " §8um §6" + time2 + " Uhr§8!");
						sender.sendMessage("   §7Grund: §8" + Main.banUtil.getCause(UUID));
						sender.sendMessage("   §7Kommentar: §8" + Main.banUtil.getComment(UUID));

					} catch (NullPointerException ex) {
					}
				} else {
					sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler war noch nicht auf dem Netzwerk!"));
				}
			} else {
				sender.sendMessage(Main.PREFIX + "§cBitte benutze: §6/tempban <Spieler> <Zeit>+Zeitangabe <Grund>§c!");
			}
		} else {
			sender.sendMessage(Main.NO_PERMISSION);
		}
	}

}
