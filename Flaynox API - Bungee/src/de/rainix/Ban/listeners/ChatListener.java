package de.rainix.Ban.listeners;

import java.text.SimpleDateFormat;

import de.rainix.Ban.main.Main;
import flaynoxapi.message.MessageManager;
import flaynoxapi.message.Placeholder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(ChatEvent e) {
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			String UUID = p.getUniqueId().toString().replaceAll("-", "");
			if (!e.getMessage().startsWith("/")) {
				if (Main.muteUtil.isPermMuted(UUID)) {
					e.setCancelled(true);
					p.sendMessage(Placeholder.replaceTimeAndReason(
							new MessageManager(p).getMessage(flaynoxapi.message.Message.MUTE_PERM), "", "",
							Main.muteUtil.getCause(UUID)));
				} else if (Main.muteUtil.isTempMuted(UUID)) {
					long time = Main.muteUtil.getUntil(UUID);
					if (time <= System.currentTimeMillis()) {
						Main.muteUtil.unmute(UUID);
						return;
					}
					e.setCancelled(true);
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
					String time1 = sdf1.format(time);
					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
					String time2 = sdf2.format(time);
					p.sendMessage(Placeholder.replaceTimeAndReason(
							new MessageManager(p).getMessage(flaynoxapi.message.Message.MUTE_TEMP), time1, time2,
							Main.muteUtil.getCause(UUID)));
				}
			}
		}
	}

	@EventHandler
	public void onCommand(ChatEvent e) {
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			String UUID = p.getUniqueId().toString().replaceAll("-", "");
			if (e.getMessage().toLowerCase().startsWith("/")) {
				if (e.getMessage().toLowerCase().startsWith("/msg ")
						|| e.getMessage().toLowerCase().startsWith("/tell ")) {
					if (Main.muteUtil.isPermMuted(UUID)) {
						e.setCancelled(true);
						p.sendMessage(Placeholder.replaceTimeAndReason(
								new MessageManager(p).getMessage(flaynoxapi.message.Message.MUTE_PERM), "", "",
								Main.muteUtil.getCause(UUID)));
					} else if (Main.muteUtil.isTempMuted(UUID)) {
						long time = Main.muteUtil.getUntil(UUID);
						if (time <= System.currentTimeMillis()) {
							Main.muteUtil.unmute(UUID);
							return;
						}
						e.setCancelled(true);
						SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
						String time1 = sdf1.format(time);
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						String time2 = sdf2.format(time);
						p.sendMessage(Placeholder.replaceTimeAndReason(
								new MessageManager(p).getMessage(flaynoxapi.message.Message.MUTE_TEMP), time1, time2,
								Main.muteUtil.getCause(UUID)));
					}
				}
			}
		}
	}

}
