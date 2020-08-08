package de.rainix.Ban.commands;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

	public UnbanCommand() {
		super("unban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("ban")) {
			if (args.length == 1) {
				String UUID = Main.uuidFetcher(args[0]).toString();
				if (UUID != null) {
					String name = Main.nameFetcher(UUID);
					if (Main.banUtil.isPermBanned(UUID)) {
						Main.banUtil.unban(UUID);
						sender.sendMessage(Main.toBase(
								Main.PREFIX + "§aDer Spieler §6" + name + "§a ist nun nicht mehr permanent gebannt!"));
					} else if (Main.banUtil.isTempBanned(UUID)) {
						Main.banUtil.unban(UUID);
						sender.sendMessage(Main.toBase(
								Main.PREFIX + "§aDer Spieler §6" + name + "§a ist nun nicht mehr zeitlich gebannt!"));
					} else {
						sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler ist nicht gebannt!"));
					}
				} else {
					sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler war noch nicht auf dem Netzwerk!"));
				}

			} else {
				sender.sendMessage(Main.toBase(Main.PREFIX + "§cBitte benutze: §6/unban <Spieler>§c!"));
			}
		} else {
			sender.sendMessage(Main.toBase(Main.NO_PERMISSION));
		}
	}

}
