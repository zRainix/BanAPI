package de.rainix.Ban.commands;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommand extends Command {

	public UnmuteCommand() {
		super("unmute");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("mute")) {
			if (args.length == 1) {
				String UUID = Main.uuidFetcher(args[0]).toString();
				if (UUID != null) {
					String name = Main.nameFetcher(UUID);
					if (Main.muteUtil.isPermMuted(UUID)) {
						Main.muteUtil.unmute(UUID);
						sender.sendMessage(Main.toBase(
								Main.PREFIX + "§aDer Spieler §6" + name + "§a ist nun nicht mehr permanent gemutet!"));
					} else if (Main.muteUtil.isTempMuted(UUID)) {
						Main.muteUtil.unmute(UUID);
						sender.sendMessage(Main.toBase(
								Main.PREFIX + "§aDer Spieler §6" + name + "§a ist nun nicht mehr zeitlich gemutet!"));
					} else {
						sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler ist nicht gemutet!"));
					}

				} else {
					sender.sendMessage(Main.toBase(Main.PREFIX + "§cDieser Spieler war noch nicht auf dem Netzwerk!"));
				}

			} else {
				sender.sendMessage(Main.toBase(Main.PREFIX + "§cBitte benutze: §6/unmute <Spieler>§c!"));
			}
		} else {
			sender.sendMessage(Main.toBase(Main.NO_PERMISSION));
		}
	}

}
