package de.rainix.Ban.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.rainix.Ban.main.Main;
import de.rainix.Ban.mysql.MySQL;
import flaynoxapi.message.MessageManager;
import flaynoxapi.message.Placeholder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanUtil {

	public boolean playerExists(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		try {
			ResultSet rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID='" + UUID + "'").executeQuery();
			if (rs.next()) {
				return rs.getString("UUID") != null;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void permbanPlayer(String UUID, String EXECUTOR, String CAUSE, String IP, String COMMENT) {
		banPlayer(UUID, EXECUTOR, CAUSE, -1, COMMENT, IP, true);
	}

	public void banPlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN) {
		if (MySQL.isConnected() != true)
			return;
		if (Main.banUtil.isBanned(UUID)) {
			Main.banUtil.unban(UUID);
		}
		ProxiedPlayer all = ProxyServer.getInstance().getPlayer(Main.nameFetcher(UUID));
		if (all != null) {
			if (UNTIL == -1) {
				all.disconnect(Placeholder.replaceTimeAndReason(
						new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_PERM), "", "", CAUSE));
			} else {
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
				String time1 = sdf1.format(UNTIL);
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
				String time2 = sdf2.format(UNTIL);
				all.disconnect(Placeholder.replaceTimeAndReason(
						new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_TEMP), time1, time2, CAUSE));
			}
		}
		try {
			PreparedStatement ps = MySQL.con.prepareStatement(
					"INSERT INTO Ban(UUID, EXECUTOR, SINCE, UNTIL, CAUSE, COMMENT, IP, IPBAN) VALUES (?,?,?,?,?,?,?,?);");
			ps.setString(1, UUID);
			ps.setString(2, EXECUTOR);
			ps.setLong(3, System.currentTimeMillis());
			ps.setLong(4, UNTIL);
			ps.setString(5, CAUSE);
			ps.setString(6, COMMENT);
			ps.setString(7, IP);
			ps.setBoolean(8, IPBAN);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void banPlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN, long SINCE) {
		if (Main.banUtil.isBanned(UUID)) {
			Main.banUtil.unban(UUID);
		}
		if (MySQL.isConnected() != true)
			return;
		ProxiedPlayer all = ProxyServer.getInstance().getPlayer(Main.nameFetcher(UUID));
		if (all.isConnected()) {
			if (UNTIL == -1) {
				all.disconnect(Placeholder.replaceTimeAndReason(
						new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_PERM), "", "", CAUSE));
			} else {
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.");
				String time1 = sdf1.format(UNTIL);
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
				String time2 = sdf2.format(UNTIL);
				all.disconnect(Placeholder.replaceTimeAndReason(
						new MessageManager(all).getMessage(flaynoxapi.message.Message.BAN_TEMP), time1, time2, CAUSE));
			}
		}
		try {
			PreparedStatement ps = MySQL.con.prepareStatement(
					"INSERT INTO Ban(UUID, EXECUTOR, SINCE, UNTIL, CAUSE, COMMENT, IP, IPBAN) VALUES (?,?,?,?,?,?,?,?);");
			ps.setString(1, UUID);
			ps.setString(2, EXECUTOR);
			ps.setLong(3, SINCE);
			ps.setLong(4, UNTIL);
			ps.setString(5, CAUSE);
			ps.setString(6, COMMENT);
			ps.setString(7, IP);
			ps.setBoolean(8, IPBAN);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tempbanPlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN) {
		banPlayer(UUID, EXECUTOR, CAUSE, UNTIL, COMMENT, IP, IPBAN);
	}

	public boolean isBanned(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!playerExists(UUID)) {
			return false;
		}
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Ban WHERE UUID='" + UUID + "'");
		try {
			while (rs.next()) {
				if (rs.getString("UUID") == null) {
					return false;
				} else {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String isIPBanned(String IP) {
		if (MySQL.isConnected() != true)
			return null;
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Ban WHERE IP='" + IP + "'");
		try {
			while (rs.next()) {
				if (rs.getString("IP") == null) {
					return null;
				} else {
					return rs.getString("UUID");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean isPermBanned(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!isBanned(UUID)) {
			return false;
		}
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Ban WHERE UUID='" + UUID + "'");
		try {
			while (rs.next()) {
				if (rs.getString("UUID") == null) {
					return false;
				} else if (rs.getLong("UNTIL") == -1) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void setComment(String UUID, String COMMENT) {
		if (MySQL.isConnected() != true)
			return;
		if (!isBanned(UUID)) {
			return;
		}
		MySQL.update("UPDATE Ban SET COMMENT='" + COMMENT + "' WHERE UUID='" + UUID + "'");
		return;
	}

	public void setIP(String UUID, String IP) {
		if (MySQL.isConnected() != true)
			return;
		if (!isBanned(UUID)) {
			return;
		}
		MySQL.update("UPDATE Ban SET IP='" + IP + "' WHERE UUID='" + UUID + "'");
		return;
	}

	public boolean isTempBanned(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!isBanned(UUID)) {
			return false;
		}
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				if (rs.getLong("UNTIL") > 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public long getLeftTime(String UUID) {
		if (MySQL.isConnected() != true)
			return 0;
		long l = 0;
		if (!playerExists(UUID))
			return 0;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getLong("UNTIL");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public long getSince(String UUID) {
		if (MySQL.isConnected() != true)
			return 0;
		long l = 0;
		if (!playerExists(UUID))
			return 0;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getLong("SINCE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public boolean getIPBanned(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		boolean l = false;
		if (!playerExists(UUID))
			return false;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getBoolean("IPBAN");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public String getCause(String UUID) {
		if (MySQL.isConnected() != true)
			return "";
		String l = "";
		if (!playerExists(UUID))
			return null;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getString("CAUSE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public String getExecutor(String UUID) {
		if (MySQL.isConnected() != true)
			return "";
		String l = "";
		if (!playerExists(UUID))
			return null;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getString("EXECUTOR");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public String getComment(String UUID) {
		if (MySQL.isConnected() != true)
			return "";
		String l = "";
		if (!playerExists(UUID))
			return null;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getString("COMMENT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public String getIP(String UUID) {
		if (MySQL.isConnected() != true)
			return "";
		String l = "";
		if (!playerExists(UUID))
			return null;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban WHERE UUID ='" + UUID + "'").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (rs.next()) {
				l = rs.getString("IP");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	public void unban(String UUID) {
		try {
			if (MySQL.isConnected() != true)
				return;
			PreparedStatement ps = null;
			try {
				ps = MySQL.con.prepareStatement("DELETE FROM Ban WHERE UUID='" + UUID + "'");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex) {
		}
	}

	public List<String> getBannendPlayers() {
		if (MySQL.isConnected() != true)
			return null;
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Ban").executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while (rs.next()) {
				try {
					try {
						list.add(rs.getString("UUID").toString());
					} catch (NullPointerException ex) {
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return list;

	}

}
