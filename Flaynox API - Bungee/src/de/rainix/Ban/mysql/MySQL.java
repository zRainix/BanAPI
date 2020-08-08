package de.rainix.Ban.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sqlapi.sql.SQLManager;

public class MySQL {
	public static Connection con;

	public static void connect() {
		if (!isConnected()) {
			try {
				if (SQLManager.isConnected()) {
					con = SQLManager.getConnection();
					System.out.println("[MySQL] Die Verbindung zu MySQL wurde von der SQL-API übernommen! {Ban}");
				}
				return;
			} catch (Exception ex) {
				System.out.println(
						"[MySQL] Die Verbindung zu MySQL konnte nicht von der SQL-API übernommen werden! {Ban}");
			}
		}
	}

	public static boolean isConnected() {
		return (con == null ? false : true);
	}

	public static Connection getConnection() {
		return con;
	}

	public static void update(String query) {
		PreparedStatement ps = null;
		try {
			ps = MySQL.getConnection().prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getString(String query, String object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = MySQL.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString(object);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int getInt(String query, String object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = MySQL.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(object);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static ArrayList<String> getStringList(String query, String object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ArrayList<String> list = new ArrayList<>();
			ps = MySQL.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(object));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static ArrayList<Integer> getIntList(String query, String object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ArrayList<Integer> list = new ArrayList<>();
			ps = MySQL.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt(object));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static ResultSet getResult(String query) {
		PreparedStatement ps;
		ResultSet rs = null;
		try {
			ps = MySQL.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getCon() {
		return con;
	}

}
