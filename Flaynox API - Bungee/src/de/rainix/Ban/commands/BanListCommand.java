package de.rainix.Ban.commands;

import java.text.SimpleDateFormat;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BanListCommand extends Command {

	public BanListCommand() {
		super("banlist");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("ban")) {
			try {
				sender.sendMessage(Main.toBase("§cGebannte Spieler: "));
				for (String bannedPlayersUUID : Main.banUtil.getBannendPlayers()) {
					if (Main.nameFetcher(bannedPlayersUUID) != null) {
						sender.sendMessage(Main.toBase("§4" + Main.nameFetcher(bannedPlayersUUID) + "§7:"));
						if (Main.banUtil.isPermBanned(bannedPlayersUUID)) {
							sender.sendMessage((Main.toBase("   §7Gebannt: §4§lPermanent!")));
						} else {
							long time = Main.banUtil.getLeftTime(bannedPlayersUUID);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
							String time1 = sdf1.format(time);
							SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
							String time2 = sdf2.format(time);
							sender.sendMessage(
									(Main.toBase("   §7Gebannt bis: §6" + time1 + " §8um §6" + time2 + " Uhr§c!")));
						}
						sender.sendMessage((Main.toBase("   §7Grund: §8" + Main.banUtil.getCause(bannedPlayersUUID))));
						sender.sendMessage(
								(Main.toBase("   §7Kommentar: §8" + Main.banUtil.getComment(bannedPlayersUUID))));
					}
				}
			} catch (NullPointerException ex) {
				sender.sendMessage(Main.toBase("§cFehler"));
			}
		} else {
			sender.sendMessage(Main.toBase(Main.NO_PERMISSION));
		}
	}
}
