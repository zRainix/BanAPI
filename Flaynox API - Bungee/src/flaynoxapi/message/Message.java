package flaynoxapi.message;

public enum Message {

	WAIT_SHORT("useful.waitshort", "§7Wait short..."), YES("useful.yes", "§aYes!"), NO("useful.no", "§cNo!"),
	AND("useful.and", "and"), BOUGHT("useful.bought", "§aBought!"), NOT_BOUGHT("useful.notbought", "§cNot bought!"),
	AVAIBLE_SOON("useful.soonavaible", "§cSoon avaible."), ERROR("useful.error", "§4Error!"),
	STOP("useful.stop", "§cStop!"), ENABLED("useful.enabled", "§2Enabled"), DISABLED("useful.disabled", "§cDisabled"),
	PLAYER_NOT_EXISTS("useful.playernotxists", "§cThe specified player does not exist."),
	FOR_EVER("useful.forever", "for ever"), NEVER("useful.never", "never"), HOURS("useful.hours", "hours"),
	MINUTES("useful.minutes", "minutes"), SECONDS("useful.seconds", "seconds"),
	BAN_PERM("ban.perm",
			"§cYou were §4§lPERMANENTLY §r§cbanned from this server!" + "\n" + "\n§7Reason: §6" + Placeholder.REASON
					+ "\n§7You may send an unban request on our website: §6flaynox.de§7!"),
	BAN_TEMP("ban.temp",
			"§cYou were §4TEMPORARILY §r§cbanned from this server!" + "\n" + "\n§7Reason: §6" + Placeholder.REASON
					+ "\n§7Banned until: §6" + Placeholder.TIME1 + "§7 at §6" + Placeholder.TIME2 + "§7 oClock"
					+ "\n§7You may send an unban request on our website: §6flaynox.de§7!"),
	MUTE_PERM("mute.perm",
			"§cYou were §4§lPERMANENTLY §r§cmuted!" + "\n" + "\n§7Reason: §6" + Placeholder.REASON),
	MUTE_TEMP("mute.temp",
			"§cYou were §4TEMPORARILY §r§cmuted!" + "\n" + "\n§7Reason: §6" + Placeholder.REASON
					+ "\n§7Muted until: §6" + Placeholder.TIME1 + "§7 at §6" + Placeholder.TIME2 + "§7 oClock"),

	NICK_NICKED("nick.nicked", "§aYou are now nicked."), NICK_UNNICKED("nick.unnicked", "nickname has been removed."),
	NICK_NICKNAME("nick.name", "§5Your nickname is: §e" + Placeholder.NICKNAME);

	private String messagePath;
	private String standardText;

	private Message(String messagePath, String standardText) {

		setMessagePath(messagePath);
		setStandardText(standardText);

	}

	public String getMessagePath() {

		return messagePath;
	}

	public void setMessagePath(String messagePath) {

		this.messagePath = messagePath;
	}

	public String getStandardText() {

		return standardText.replaceAll("§", "&");
	}

	public void setStandardText(String standardText) {

		this.standardText = standardText;
	}

}
