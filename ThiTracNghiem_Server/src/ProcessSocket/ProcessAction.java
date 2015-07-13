/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessSocket;

import Account.AccountDAO;
import Entity.*;
import Question.QuestionDAO;
import Submit.SubmitDAO;
import Work.Convert;
import Work.DateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class ProcessAction {

    private Convert convert = new Convert();
    private SubmitDAO subDAO = new SubmitDAO();
    private AccountDAO accDAO = new AccountDAO();
    private QuestionDAO quesDAO = new QuestionDAO();

    private Vector<Question> vtQuestion = null;
    private int idTask = 0;
    private final String[] ACTION = {
        // Exit; Message; Login; logout; register acc; change password; 
        "EXIT", "MESG", "LGIN", "LOUT", "RACC", "CPWD",//5
        "RQQU", "RSQU", "RQRS", "RSRS", // Req Question, Res Question, Req Result, Res Result
        "RQHI", "RSHI","RQDT"}; // Req, Res History

    public Object exit(byte[] data) {

        return data;
    }

    public Object message(byte[] data) {

        return data;
    }

    public byte[] logIn(byte[] data) {
        byte[] packet = null;
        try {
            Account reqAcc = (Account) convert.byteToObject(data);
            Account resAcc = accDAO.checkLogin(reqAcc.getUsername(), reqAcc.getPassword());
            String msg = new String();
            if (resAcc != null) {
                if (resAcc.isOnline()) {
                    msg = "User is online";
                    System.out.println(msg);
                    packet = convert.allToPacket(ACTION[1], msg);
                } else {
                    packet = convert.allToPacket(ACTION[2], resAcc);
                    resAcc.setLastTimeLogin(DateTime.getTime() + " " + DateTime.getToday());
                    resAcc.setOnline(true);
                    accDAO.updateAccount(resAcc);
                }
            } else {
                msg = "Username, password is wrong!";
                System.out.println(msg);
                packet = convert.allToPacket(ACTION[1], msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    public void logOut(byte[] data) {
        try {
            String username = (String) convert.byteToObject(data);
            System.out.println("Username logout: " + username);
            accDAO.logout(username);
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] registerAcc(byte[] data) {
        byte[] packet = null;
        try {
            String msg = new String();
            Account reqAcc = (Account) convert.byteToObject(data);
            if (!accDAO.checkUser(reqAcc.getUsername())) {
                System.out.println(reqAcc.getUsername() + "\n" + reqAcc.getPassword() + "\n==============\n");
                accDAO.addAccount(reqAcc);
                msg = "Register successful";
                packet = convert.allToPacket(ACTION[1], msg);
            } else {
                msg = "Username is already exit";
                packet = convert.allToPacket(ACTION[1], msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    public byte[] changePWD(byte[] data) {
        byte[] packet = null;
        try {
            Account reqAcc = (Account) convert.byteToObject(data);
            if (accDAO.changePass(reqAcc.getUsername(), reqAcc.getPassword())) {
                String msg = new String();
                msg = "Password has changed!";
                packet = convert.allToPacket(ACTION[1], msg);
            } else {
                String msg = new String();
                msg = "Password has not change!";
                packet = convert.allToPacket(ACTION[1], msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    // Yêu cầu lấy đề thi
    public byte[] registerQuesstion(byte[] data) {
        byte[] packet = null;
        try {
            Register regiser = (Register) convert.byteToObject(data);
            // Ghi vào CSDL thông tin đăng kí, thông tin bài làm. - Update
            idTask = subDAO.register(regiser);
            // Lấy bộ ngẫu nhiên câu hỏi
            vtQuestion = quesDAO.getRandomQuestion(regiser.getIdSubject(), regiser.getLevel());
            if (vtQuestion != null) {
                packet = convert.allToPacket(ACTION[7], vtQuestion);
            } else {
                String msg = "Database is empty!";
                packet = convert.allToPacket(ACTION[1], msg);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    // Yêu cầu nộp bài, chấm điểm
    public byte[] reqSubmit(byte[] data) {
        byte[] packet = null;
        try {
            Vector<String> vtAnswer = (Vector<String>) convert.byteToObject(data);
            int total = 0;
            boolean correct = false;
            if (vtQuestion != null) {
                for (int i = 0; i < vtQuestion.size(); i++) {
                    Question ques = vtQuestion.get(i);
                    String answer = vtAnswer.get(i);
                    if(answer.equalsIgnoreCase(ques.getCorrect())){
                        total += 1;
                        correct = true;
                    }else{
                        correct = false;
                    }
                    // Lưu thông tin chi tiết từng câu hỏi
                    Detail detail = new Detail(idTask, ques.getID(), answer, correct);
                    subDAO.addDetail(detail);
                }
                subDAO.updateTask(idTask, total);
                packet = convert.allToPacket(ACTION[9], total);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    public byte[] reqHistory(byte[] data) {
        byte[] packet = null;
        try {
            String user = (String) convert.byteToObject(data);
            Vector<History> vt = subDAO.getHistory(user);
            packet = convert.allToPacket(ACTION[11], vt);
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }
    
    public byte[] reqShowDetail(byte[] data) {
        byte[] packet = null;
        try {
            int idTask = (Integer) convert.byteToObject(data);
            System.out.println("Idtask: "+idTask);
            Vector<DetailHistory> vt = subDAO.showDetail(idTask);
            packet = convert.allToPacket(ACTION[12], vt);
        } catch (Exception ex) {
            Logger.getLogger(ProcessAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

}
