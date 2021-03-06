package de.rainix.Ban.commands;

import java.sql.SQLException;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import sqlapi.player.PlayerProfile;

@SuppressWarnings("deprecation")
public class PermBanCommand extends Command {

	public PermBanCommand() {
		super("permban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission("ban")) {
			if (args.length >= 2) {
				String UUID = Main.uuidFetcher(args[0]);
				if (UUID != null) {
					String name = Main.nameFetcher(UUID);
					String CAUSE = "";
					String ip = "";
					try {
						ip += new PlayerProfile(UUID).getIP();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ip.equalsIgnoreCase("ip")) {
						ip = "";
					}
					for (int i = 1; i < args.length; i++) {
						CAUSE = CAUSE + args[i] + " ";
					}
					Main.banUtil.permbanPlayer(UUID, sender.getName(), CAUSE, "", ip);
					sender.sendMessage(Main.PREFIX + "�aDu hast erfolgreich den Spieler �6" + name
							+ "�4�l PERMANENT�r�a von diesem Netzwerk wegen �6" + CAUSE + "�a gebannt!");
					try {
						sender.sendMessage("�4" + name + "�7:");
						sender.sendMessage("   �7Gebannt: �4�lPermanent�r�8!");
						sender.sendMessage("   �7Grund: �8" + Main.banUtil.getCause(UUID));
						sender.sendMessage("   �7Kommentar: �8" + Main.banUtil.getComment(UUID));

					} catch (NullPointerException ex) {
					}
				} else {
					sender.sendMessage(Main.toBase(Main.PREFIX + "�cDieser Spieler war noch nicht auf dem Netzwerk!"));
				}
			} else {
				sender.sendMessage(Main.PREFIX + "�cBitte benutze: �6/permban <Spieler> <Grund>�c!");
			}
		} else {
			sender.sendMessage(Main.NO_PERMISSION);
		}
	}

}
