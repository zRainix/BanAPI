package de.rainix.Ban.commands;

import java.text.SimpleDateFormat;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MuteListCommand extends Command {

	public MuteListCommand() {
		super("mutelist");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("mute")) {
			try {
				sender.sendMessage(Main.toBase("§cGemutete Spieler: "));
				for (String mutedPlayersUUID : Main.muteUtil.getMutenendPlayers()) {
					if (Main.nameFetcher(mutedPlayersUUID) != null) {
						sender.sendMessage(Main.toBase("§4" + Main.nameFetcher(mutedPlayersUUID) + "§7:"));
						if (Main.muteUtil.isPermMuted(mutedPlayersUUID)) {
							sender.sendMessage((Main.toBase("   §7Gemutet: §4§lPermanent!")));
						} else {
							long time = Main.muteUtil.getUntil(mutedPlayersUUID);
							SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
							String time1 = sdf1.format(time);
							SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
							String time2 = sdf2.format(time);
							sender.sendMessage(
									(Main.toBase("   §7Gemutet bis: §6" + time1 + " §8um §6" + time2 + " Uhr§c!")));
						}
						sender.sendMessage(
								(Main.toBase("   §7Grund: §8" + Main.muteUtil.getCause(mutedPlayersUUID))));
						sender.sendMessage(
								(Main.toBase("   §7Kommentar: §8" + Main.muteUtil.getComment(mutedPlayersUUID))));
					}
				}
			} catch (NullPointerException ex) {
				sender.sendMessage(Main.toBase("§cFehler"));
			}
		}
	}
}
