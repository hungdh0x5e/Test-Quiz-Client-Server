package DBConnect;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class DatabaseConnect {

    public static String url;
    public static String driver;
    public static String servername;
    public static String database;
    public static String user;
    public static String pwd;
    public static String port;
    public static Connection connection;
    public static Statement statement;

    public static boolean connect() {
        try {

            Properties p = new Properties();
            p.load(new FileInputStream("database.properties"));
            driver = p.getProperty("driver");
            servername = p.getProperty("servername");
            database = p.getProperty("database");
            user = p.getProperty("user");
            pwd = p.getProperty("password");
            port = p.getProperty("port");
            url = "jdbc:sqlserver://" + servername + ":" + port + ";databaseName=" + database;
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pwd);
            if (connection == null) {
                throw new NullPointerException("Can not connect to SQL server!");
            }
        } catch (IOException ex) {
            System.out.println("Error(IO): " + ex.toString() + "\n");
            JOptionPane.showMessageDialog(null, "Can not connect to server.\nPlease try again!", "ERROR!", 0);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: Class not found!\n");
            JOptionPane.showMessageDialog(null, "Can not connect to server.\nPlease try again!", "ERROR!", 0);
        } catch (SQLException ex) {
            System.out.println("Error(SQL): " + ex.toString());
            JOptionPane.showMessageDialog(null, "Can not connect to server.\nPlease try again!", "ERROR!", 0);
        }
        return true;
    }

    public static ResultSet getData(String sqlCommand) {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlCommand);
            return rs;
        } catch (SQLException ex) {
            System.out.println("Error!\n" + ex.toString());
            return null;
        }
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        close(ps);
    }
    
}
