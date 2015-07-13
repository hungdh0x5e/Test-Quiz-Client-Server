/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Submit;

import DBConnect.DatabaseConnect;
import Entity.Detail;
import Entity.DetailHistory;
import Entity.History;
import Entity.Register;
import Question.QuestionDAO;
import Work.DateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class SubmitDAO {

    private DatabaseConnect DB = new DatabaseConnect();
    private String sqlCommand;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int idTask = 0;

    public int register(Register reg) {
        int idRegister = 0;
        if (DB.connect()) {
            try {
                // Insert register info
                sqlCommand = "Insert into tblRegister VALUES(?,?,?)";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, reg.getUsername());
                ps.setString(2, reg.getIdSubject());
                ps.setNString(3, reg.getLevel());
                int row = ps.executeUpdate();
                // Get idRegister
                sqlCommand = "Select MAX(idRegister) From tblRegister";
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                while (rs.next()) {
                    idRegister = rs.getInt(1);
                }
                System.out.println("IdRegiser: " + idRegister);
                // Insert task info
                sqlCommand = "Insert into tblTask VALUES(?,?,?)";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setInt(1, idRegister);
                ps.setString(2, DateTime.getTime() + " " + DateTime.getToday());
                ps.setInt(3, 0);
                row = ps.executeUpdate();
                // Get idTask
                sqlCommand = "Select MAX(idTask) From tblTask";
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                while (rs.next()) {
                    idTask = rs.getInt(1);
                    System.out.println("IdRegister: " + idRegister + "   IdTask: " + idTask);
                }
            } catch (SQLException ex) {
                Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return idTask;
    }

    public void updateTask(int idTask, int total) {
        if (DB.connect()) {
            sqlCommand = "Update tblTask set total =? where idTask=?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setInt(1, total);
                ps.setInt(2, idTask);
                int row = ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(SubmitDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps);
            }
        }
    }

    public void addDetail(Detail detail) {
        if (DB.connect()) {
            sqlCommand = "Insert into tblDetail Values(?,?,?,?)";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setInt(1, detail.getIdTask());
                ps.setString(2, detail.getIdQuestion());
                ps.setString(3, detail.getAnswer());
                ps.setBoolean(4, detail.isCorrect());
                int row = ps.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("Add Detail: \n" + ex.toString());
            } finally {
                DB.close(ps);
            }

        }
    }

    public Vector<DetailHistory> showDetail(int idTask) {
        Vector<DetailHistory> vt = null;
        if (DB.connect()) {
            sqlCommand = "SELECT question,answerA,answerB,answerC,answerD,ct.answer, q.correct"
                    + " FROM ((tblTask bl INNER JOIN tblRegister dk ON bl.idRegister = dk.idRegister) "
                    + " INNER JOIN tblDetail ct ON bl.idTask = ct.idTask) "
                    + " INNER JOIN tblQuestion q ON q.idQuestion = ct.idQuestion"
                    + " WHERE bl.idTask = ?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setInt(1, idTask);
                rs = ps.executeQuery();
                vt = new Vector<>();
                int stt = 1;
                while (rs.next()) {
                    DetailHistory dh = new DetailHistory(stt++ + ". " + rs.getNString(1), rs.getNString(2), rs.getNString(3),
                            rs.getNString(4), rs.getNString(5), rs.getString(6), rs.getNString(7));
                    vt.add(dh);
                }
                return vt;
            } catch (SQLException ex) {
                System.out.println("Add Detail: \n" + ex.toString());
            } finally {
                DB.close(ps);
            }
        }
        return null;
    }

    public Vector<History> getHistory(String user) {
        Vector<History> vt = null;
        if (DB.connect()) {
            sqlCommand = "SELECT idTask,sb.nameSubject,level,timeTask,total "
                    + " FROM tblTask bl INNER JOIN tblRegister dk ON bl.idRegister = dk.idRegister "
                    + " INNER JOIN tblSubject sb ON sb.idSubject = dk.idSubject"
                    + " WHERE username = ?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, user);
                rs = ps.executeQuery();
                vt = new Vector<>();
                while (rs.next()) {
                    History hs = new History(rs.getInt(1), rs.getNString(2), rs.getNString(3), rs.getString(4), rs.getInt(5));
                    vt.add(hs);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SubmitDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vt;
    }

    public static void main(String[] args) {
        SubmitDAO subDAO = new SubmitDAO();
        Vector<DetailHistory> vt = new Vector<>();
        vt = subDAO.showDetail(1);
        for (DetailHistory x : vt) {
            System.out.println(x.getIdQuestion());
        }
    }
}
