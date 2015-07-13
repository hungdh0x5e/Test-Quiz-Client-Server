/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Question;

import DBConnect.DatabaseConnect;
import Entity.Question;
import Entity.Subject;
import Work.Convert;
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
public class QuestionDAO {

    DatabaseConnect DB = new DatabaseConnect();
    private String sqlCommand;
    public String nameTable = "tblQuestion";
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public Vector<Subject> getNameSubject() {
        Vector<Subject> vt = new Vector<>();
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from tblSubject";
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                while (rs.next()) {
                    vt.add(new Subject(rs.getString(1), rs.getNString(2)));
                }
            } catch (SQLException ex) {
                Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return vt;
    }

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
                Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }
        }
        return columnName;
    }

    public Vector<Question> getRowData(String name) {
        Vector<Question> vt = null;
        if (DB.connect()) {
            try {
                sqlCommand = "Select * from " + nameTable + " where idSubject = '" + name + "'";
                ps = DB.connection.prepareStatement(sqlCommand);
                rs = ps.executeQuery();
                vt = new Vector<>();
                while (rs.next()) {
                    Question mmt = new Question(rs.getString(1), rs.getNString(2), rs.getNString(3),
                            rs.getNString(4), rs.getNString(5), rs.getNString(6), rs.getNString(7), rs.getNString(8), rs.getString(9));

                    vt.addElement(mmt);
                }
            } catch (SQLException ex) {
                System.out.println("Error getRowData_MMTDAO.\n" + ex.toString());
            } finally {
                DB.close(ps, rs);
            }
        }
        return vt;
    }

    public boolean addQuestion(Question mmt) {
        boolean kt = true;
        if (DB.connect()) {
            try {
                sqlCommand = "Insert into " + nameTable + " values(?,?,?,?,?,?,?,?)";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, mmt.getID());
                ps.setNString(2, mmt.getQuestion());
                ps.setNString(3, mmt.getAnswerA());
                ps.setNString(4, mmt.getAnswerB());
                ps.setNString(5, mmt.getAnswerC());
                ps.setNString(6, mmt.getAnswerD());
                ps.setNString(7, mmt.getCorrect());
                ps.setNString(8, mmt.getLevel());
                ps.setString(9, mmt.getMaMH());
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                System.out.println("Error addQuestion_MMTDAO.\n" + ex.toString());
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean updateQuestion(Question mmt) {
        boolean kt = true;
        if (DB.connect()) {
            sqlCommand = "update " + nameTable + " set question=?,answerA=?,answerB=?,answerC=?,"
                    + "answerD=?,correct=?,level=? where idQuestion=?";
            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(8, mmt.getID());
                ps.setNString(1, mmt.getQuestion());
                ps.setNString(2, mmt.getAnswerA());
                ps.setNString(3, mmt.getAnswerB());
                ps.setNString(4, mmt.getAnswerC());
                ps.setNString(5, mmt.getAnswerD());
                ps.setNString(6, mmt.getCorrect());
                ps.setNString(7, mmt.getLevel());
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                System.out.println("Error updateQuestion_MMTDAO.\n" + ex.toString());
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean delQuestion(String ID) {
        boolean kt = true;
        if (DB.connect()) {
            try {
                sqlCommand = "Delete from " + nameTable + " where idQuestion=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, ID);
                int row = ps.executeUpdate();
                if (row < 1) {
                    kt = false;
                }
            } catch (SQLException ex) {
                System.out.println("Error delQuestion_MMTDAO.\n" + ex.toString());
            } finally {
                DB.close(ps);
            }
        }
        return kt;
    }

    public boolean checkIDQuestion(String ID) {
        boolean kt = false;
        if (DB.connect()) {
            try {
                sqlCommand = "Select ID from " + nameTable + " where idQuestion=?";
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setString(1, ID);
                rs = ps.executeQuery();
                while (rs.next()) {
                    kt = true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return kt;
    }

    public Vector<Question> getRandomQuestion(String idSubject, String level) {
        Vector<Question> vt = null;
        int easy = 0, medium = 0, hard = 0;
        switch (level) {
            case "Dễ":
                easy = 6;
                medium = 3;
                hard = 1;
                break;
            case "Trung Bình":
                easy = 4;
                medium = 4;
                hard = 2;
                break;
            case "Khó":
                easy = 3;
                medium = 3;
                hard = 4;
                break;
        }
        if (DB.connect()) {
            sqlCommand = "SELECT idQuestion,question,answerA,answerB,answerC,answerD,correct FROM " + nameTable
                    + " where idQuestion in(select top(?) idQuestion from " + nameTable + " where [level]=N'Dễ'and idSubject= ? order by NEWID())"
                    + " UNION ALL "
                    + " SELECT idQuestion,question,answerA,answerB,answerC,answerD,correct FROM " + nameTable
                    + " where idQuestion in(select top(?) idQuestion from " + nameTable + " where [level]=N'Trung Bình'and idSubject= ? order by NEWID())"
                    + " UNION ALL "
                    + " SELECT idQuestion,question,answerA,answerB,answerC,answerD,correct FROM " + nameTable
                    + " where idQuestion in(select top(?) idQuestion from " + nameTable + " where [level]=N'Khó'and idSubject= ? order by NEWID())"
                    + " order by idQuestion";

            try {
                ps = DB.connection.prepareStatement(sqlCommand);
                ps.setInt(1, easy);
                ps.setString(2, idSubject);
                ps.setInt(3, medium);
                ps.setString(4, idSubject);
                ps.setInt(5, hard);
                ps.setString(6, idSubject);
                rs = ps.executeQuery();
                vt = new Vector<Question>();
                while (rs.next()) {
                    Question ques = new Question(rs.getString(1), rs.getNString(2), rs.getNString(3),
                            rs.getNString(4), rs.getNString(5), rs.getNString(6), rs.getString(7));
                    vt.add(ques);
                }
            } catch (SQLException ex) {
                Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DB.close(ps, rs);
            }

        }
        return vt;
    }

    public static void main(String[] args) {
        QuestionDAO mmtdao = new QuestionDAO();
        Vector<Question> vtMMT = mmtdao.getRandomQuestion("MMT", "Trung Bình");
        String tmp;
//        for (Question mmt : vtMMT) {
        try {
            System.out.println("Total Size: " + new Convert().allToPacket("ABCD", vtMMT).length);
        } catch (Exception ex) {
            Logger.getLogger(QuestionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
//        }

    }
}
