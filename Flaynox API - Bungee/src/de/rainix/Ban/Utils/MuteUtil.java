package de.rainix.Ban.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.rainix.Ban.main.Main;
import de.rainix.Ban.mysql.MySQL;

public class MuteUtil {

	public boolean playerExists(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		try {
			ResultSet rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID='" + UUID + "'").executeQuery();
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

	public void permmutePlayer(String UUID, String EXECUTOR, String CAUSE, String IP, String COMMENT) {
		mutePlayer(UUID, EXECUTOR, CAUSE, -1, COMMENT, IP, true);
	}

	public void mutePlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN) {
		if (MySQL.isConnected() != true)
			return;
		if (Main.muteUtil.isMuted(UUID)) {
			Main.muteUtil.unmute(UUID);
		}
		try {
			PreparedStatement ps = MySQL.con.prepareStatement(
					"INSERT INTO Mute(UUID, EXECUTOR, SINCE, UNTIL, CAUSE, COMMENT, IP, IPBAN) VALUES (?,?,?,?,?,?,?,?);");
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

	public void mutePlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN, long SINCE) {
		if (Main.muteUtil.isMuted(UUID)) {
			Main.muteUtil.unmute(UUID);
		}
		if (MySQL.isConnected() != true)
			return;
		try {
			PreparedStatement ps = MySQL.con.prepareStatement(
					"INSERT INTO Mute(UUID, EXECUTOR, SINCE, UNTIL, CAUSE, COMMENT, IP, IPBAN) VALUES (?,?,?,?,?,?,?,?);");
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

	public void tempmutePlayer(String UUID, String EXECUTOR, String CAUSE, long UNTIL, String COMMENT, String IP,
			boolean IPBAN) {
		mutePlayer(UUID, EXECUTOR, CAUSE, UNTIL, COMMENT, IP, IPBAN);
	}

	public boolean isMuted(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!playerExists(UUID)) {
			return false;
		}
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Mute WHERE UUID='" + UUID + "'");
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

	public String isIPMuted(String IP) {
		if (MySQL.isConnected() != true)
			return null;
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Mute WHERE IP='" + IP + "'");
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

	public boolean isPermMuted(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!isMuted(UUID)) {
			return false;
		}
		ResultSet rs = null;
		rs = MySQL.getResult("SELECT * FROM Mute WHERE UUID='" + UUID + "'");
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
		if (!isMuted(UUID)) {
			return;
		}
		MySQL.update("UPDATE Mute SET COMMENT='" + COMMENT + "' WHERE UUID='" + UUID + "'");
		return;
	}

	public void setIP(String UUID, String IP) {
		if (MySQL.isConnected() != true)
			return;
		if (!isMuted(UUID)) {
			return;
		}
		MySQL.update("UPDATE Mute SET IP='" + IP + "' WHERE UUID='" + UUID + "'");
		return;
	}

	public boolean isTempMuted(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		if (!isMuted(UUID)) {
			return false;
		}
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID='" + UUID + "'").executeQuery();
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

	public long getUntil(String UUID) {
		if (MySQL.isConnected() != true)
			return 0;
		long l = 0;
		if (!playerExists(UUID))
			return 0;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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

	public boolean getIPMuted(String UUID) {
		if (MySQL.isConnected() != true)
			return false;
		boolean l = false;
		if (!playerExists(UUID))
			return false;
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute WHERE UUID ='" + UUID + "'").executeQuery();
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

	public void unmute(String UUID) {
		try {
			if (MySQL.isConnected() != true)
				return;
			PreparedStatement ps = null;
			try {
				ps = MySQL.con.prepareStatement("DELETE FROM Mute WHERE UUID='" + UUID + "'");
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

	public List<String> getMutenendPlayers() {
		if (MySQL.isConnected() != true)
			return null;
		List<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = MySQL.con.prepareStatement("SELECT * FROM Mute").executeQuery();
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
