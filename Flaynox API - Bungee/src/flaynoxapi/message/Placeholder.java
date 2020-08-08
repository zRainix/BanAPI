package flaynoxapi.message;

import de.rainix.Ban.main.Main;
import net.md_5.bungee.api.chat.BaseComponent;

public class Placeholder {

	public static final String PLAYER = "%Player%", TIME1 = "%Time1%", TIME2 = "%Time2%", REASON = "%Reason%",
			NICKNAME = "%Nickname%";

	private Placeholder() {

	}

	public static BaseComponent[] replaceTimeAndReason(String text, String time1, String time2, String reason) {
		if (text.contains(TIME1)) {
			text = text.replaceAll(TIME1, time1);
		}
		if (text.contains(TIME2)) {
			text = text.replaceAll(TIME2, time2);
		}
		if (text.contains(REASON)) {
			text = text.replaceAll(REASON, reason);
		}
		return Main.toBase(text);
	}

}
