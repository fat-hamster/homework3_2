package Server;

import java.sql.*;

public class DBAccess {
    private final String url = "jdbc:sqlite:src/main/resources/users.db";
    private final String auth = "SELECT NICK FROM USERS WHERE LOGIN=? AND PASSWORD=?";
    private final String isNickBusy = "SELECT * FROM USERS WHERE NICK=?";
    private final String changeNick = "UPDATE USERS SET NICK=? WHERE NICK=?";
    private Connection connection = null;
    private String error = "";

    public String getError() {
        return error;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNick(String login, String password) {
        try {
            String nick = "";
            PreparedStatement ps = connection.prepareStatement(auth);
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                nick = rs.getString(1);
                System.out.println(nick);
            }
            if(!nick.isEmpty()) {
                return nick;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean changeNick(String oldNick, String newNick) {
        try {
            PreparedStatement ps = connection.prepareStatement(isNickBusy);
            ps.setString(1, newNick);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                this.error = "Ник занят";
                return false;
            }
            ps = connection.prepareStatement(changeNick);
            ps.setString(1, newNick);
            ps.setString(2, oldNick);
            ps.execute();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConnection() {
        try {
            if(connection != null) {
                System.out.println("Connection to SQLite close.");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
