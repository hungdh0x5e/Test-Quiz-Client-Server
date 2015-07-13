/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Account;

import DBConnect.DatabaseConnect;
import Entity.Account;
import Work.MD5Encryption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class AccountDAO {

    DatabaseConnect DB = new DatabaseConnect();
    private String sqlCommand;
    private static String nameTable = "tblAccount";
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private MD5Encryption md5 = new MD5Encryption();

    public Vector<String> getColumnName() {
        Vector<String> columnName = null;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable;
                rs = DB.getData(sqlCommand);
                ResultSetMetaData rsMD = rs.getMetaData();
                int columnNo = rsMD.getColumnCount();
                columnName = new Vector<>();
                for (int i = 0; i < columnNo; i++) {
                    columnName.addElement(rsMD.getColumnName(i + 1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return columnName;
    }

    public Vector<Account> getRowData() {
        Vector<Account> vt = null;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable;
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                vt = new Vector<>();
                while (rs.next()) {
                    Account acc = new Account();
                    acc.setUsername(rs.getString(1));
                    acc.setPassword(rs.getString(2));
                    acc.setPermission(rs.getString(3));
                    acc.setLastTimeLogin(rs.getString(4));
                    acc.setFullname(rs.getString(5));
                    acc.setOnline(rs.getBoolean(6));
                    vt.addElement(acc);
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return vt;
    }
    public String getUserOnline(){
         String result="";
        if (DB.connect()) {
            try {               
                String sqlCommand = "Select count(online) from " + nameTable + " where online=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, "true");
                rs = ps.executeQuery();
                while (rs.next()) {
                    int onl =rs.getInt(1); 
                    result = onl>9?""+onl:"0"+onl;
                }
                sqlCommand = "Select count(username) from " + nameTable;
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int total =rs.getInt(1); 
                    result += total>9?""+total:"0"+total;
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }            
        }
        return result;
    }
    public Vector<Account> findByUsername(String user) {
        Vector<Account> vt = null;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable + " where username=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, user);
                rs = ps.executeQuery();
                vt = new Vector<Account>();
                while (rs.next()) {
                    Account acc = new Account();
                    acc.setUsername(rs.getString(1));
                    acc.setPassword(rs.getString(2));
                    acc.setPermission(rs.getString(3));
                    acc.setLastTimeLogin(rs.getString(4));
                    acc.setFullname(rs.getString(5));
                    acc.setOnline(rs.getBoolean(6));
                    vt.add(acc);
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return vt;
    }

    public Vector<Account> findByPermission(String permission) {
        Vector<Account> vt = null;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable + " where permission=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, permission);
                rs = ps.executeQuery();
                vt = new Vector<Account>();
                while (rs.next()) {
                    Account acc = new Account();
                    acc.setUsername(rs.getString(1));
                    acc.setPassword(rs.getString(2));
                    acc.setPermission(rs.getString(3));
                    acc.setLastTimeLogin(rs.getString(4));
                    acc.setFullname(rs.getString(5));
                    
                    vt.add(acc);
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return vt;
    }

    public Account checkLogin(String user, String pwd) {
        Vector<Account> vtAcc = findByUsername(user);
        Account acc = null;
        if (vtAcc.size() > 0) {
            acc = vtAcc.get(0);
            if (!acc.getPassword().equalsIgnoreCase(MD5Encryption.encrypt(pwd))) {
                acc = null;
            }
        }
        return acc;
    }

    public void logout(String user){
        if (DB.connect()) {
            sqlCommand = "update " + nameTable + " set online=? where username=?";
            try {            
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setBoolean(1, false);
                ps.setString(2, user);
                int row = ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }
            
    }
    
    public boolean changePass(String user, String newPwd) {
        boolean kt = false;
        if (DB.connect()) {
            sqlCommand = "Update " + nameTable + " set password=? where username=?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, MD5Encryption.encrypt(newPwd));
                ps.setString(2, user);
                int row = ps.executeUpdate();
                if (row > 0) {
                    kt = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps);
            }

        }
        return kt;
    }

    public boolean addAccount(Account acc) {
        boolean kt = true;
        if (DB.connect()) {
            try {
                sqlCommand = "Insert into " + nameTable + " values(?,?,?,?,?,?)";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, acc.getUsername());
                ps.setString(2, MD5Encryption.encrypt(acc.getPassword())); 
                ps.setString(3, acc.getPermission());
                ps.setString(4, acc.getLastTimeLogin());
                ps.setNString(5, acc.getFullname());
                ps.setBoolean(6, acc.isOnline());
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean updateAccount(Account acc) {
        boolean kt = true;
        if (DB.connect()) {
            sqlCommand = "update " + nameTable + " set password=?,permission=?,lastTime=?,fullname=?,online=? where username=?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, acc.getPassword());
                ps.setString(2, acc.getPermission());
                ps.setString(3, acc.getLastTimeLogin());
                ps.setNString(4, acc.getFullname());                
                ps.setBoolean(5, acc.isOnline());
                ps.setString(6, acc.getUsername());
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean delAccount(String usr) {
        boolean kt = true;
        if (DB.connect()) {
            try {
                sqlCommand = "Delete from " + nameTable + " where username=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, usr);
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean checkUser(String username) {
        boolean kt = false;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable + " where username=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, username);
                rs = ps.executeQuery();
                while (rs.next()) {
                    kt = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return kt;
    }

    public static void main(String[] args) {
        AccountDAO acc = new AccountDAO();
        String tmp = acc.getUserOnline();
        System.out.println(tmp.substring(0));
        System.out.println(tmp.substring(0,2));
        System.out.println(tmp.substring(2,4));

    }
}
