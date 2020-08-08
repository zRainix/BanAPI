package de.rainix.Ban.listeners;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import de.rainix.Ban.main.Main;
import flaynoxapi.message.MessageManager;
import flaynoxapi.message.Placeholder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import sqlapi.player.PlayerProfile;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(LoginEvent e) {
		String UUID = e.getConnection().getUniqueId().toString().replaceAll("-", "");
		if (Main.banUtil.isBanned(UUID)) {
			if (Main.banUtil.isPermBanned(UUID)) {
				e.getConnection()
						.disconnect(Placeholder.replaceTimeAndReason(new MessageManager(e.getConnection().getUniqueId())
								.getMessage(flaynoxapi.message.Message.BAN_PERM), "", "", Main.banUtil.getCause(UUID)));
			} else {
				long time = Main.banUtil.getLeftTime(UUID);
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
				String time1 = sdf1.format(time);
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
				String time2 = sdf2.format(time);
				e.getConnection()
						.disconnect(Placeholder.replaceTimeAndReason(
								new MessageManager(e.getConnection().getUniqueId())
										.getMessage(flaynoxapi.message.Message.BAN_TEMP),
								time1, time2, Main.banUtil.getCause(UUID)));
			}
		} else {
			String s;
			try {
				s = Main.banUtil.isIPBanned(new PlayerProfile(UUID).getIP());
				if (s != null) {
					Main.banUtil.banPlayer(UUID, "AutoBan (IP)", "Banumgehung", Main.banUtil.getLeftTime(s),
							Main.banUtil.getComment(s), new PlayerProfile(UUID).getIP(), false);
					e.getConnection().disconnect(Main.toBase("Error"));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
