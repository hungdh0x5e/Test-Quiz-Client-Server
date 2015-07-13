/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessSocket;

import Entity.Account;
import Entity.DetailHistory;
import Entity.History;
import Entity.Question;
import Entity.Register;
import Work.Convert;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class Client {

    private Convert convert = new Convert();
    private Socket socket = null;
    private OutputStream os = null;
    private InputStream is = null;
    private boolean online = false;

    private final String[] ACTION = {
        // Exit; Message; Login; logout; register acc; change password; 
        "EXIT", "MESG", "LGIN", "LOUT", "RACC", "CPWD",
        "RQQU", "RSQU", "RQRS", "RSRS", // Req Question, Res Question, Req Result, Res Result
        "RQHI", "RSHI", "RQDT"}; // Req, Res History
    private String action = "";
    private byte[] data = null;
    private int data_length = 0;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void connect() {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("config.properties"));
            String ipaddr = p.getProperty("ipaddress");
            int port = Integer.parseInt(p.getProperty("port"));
            socket = new Socket(ipaddr, port);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            // Tạo kết nối đến server và nhận trả lời.
            String msg;
            byte[] packetReceive = new byte[1024];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[0])) {
                    msg = (String) convert.byteToObject(data);
                    JOptionPane.showMessageDialog(null, msg, "Lỗi", 0);
                    return;
                }
            }
            setOnline(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can not connect to server!", "   Error", 0);
            setOnline(false);
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            byte[] packetSend = convert.allToPacket(ACTION[0], "I want disconnect!");
            os.write(packetSend);
            os.close();
            is.close();
            socket.close();
            setOnline(false);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Account reqLogin(Account reqAcc) {
        String msg = new String();
        Account resAcc = null;
        try {
            // Sending data to server
            byte[] packetSend = convert.allToPacket(ACTION[2], reqAcc);
            os.write(packetSend);

            // Receiving data from server
            byte[] packetReceive = new byte[1024];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[1])) {
                    msg = (String) convert.byteToObject(data);
                    JOptionPane.showMessageDialog(null, msg, "   Error", 0);
                    close();
                } else {
                    if (action.equals(ACTION[1])) {
                        msg = (String) convert.byteToObject(data);
                        JOptionPane.showMessageDialog(null, msg, "   Error", 0);
                        close();
                    } else {
                        resAcc = (Account) convert.byteToObject(data);
                    }
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server is disconnect!", "   Error", 0);
            setOnline(false);
            System.out.println(ex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return resAcc;
    }

    public boolean resgisterAccount(Account reqAcc) {
        boolean check = false;
        try {
            // Sending
            byte[] packetSend = convert.allToPacket(ACTION[4], reqAcc);
            os.write(packetSend);

            //Receiving
            byte[] packetReceive = new byte[1024];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[1])) {
                    String msg = (String) convert.byteToObject(data);
                    JOptionPane.showMessageDialog(null, msg);
                    check = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Có lỗi xảy ra!");
                    check = false;
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Server is disconnect!", "   Error", 0);
            setOnline(false);
            System.out.println(ex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return check;
    }

    public void reqLogout(String username) {
        try {
            // Sending
            byte[] packetSend = convert.allToPacket(ACTION[3], username);
            os.write(packetSend);
            close();
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changePass(Account acc) {
        try {
            byte[] packetSend = convert.allToPacket(ACTION[5], acc);
            os.write(packetSend);

            byte[] packetReceive = new byte[1024];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[1])) {
                    String msg = (String) convert.byteToObject(data);
                    JOptionPane.showMessageDialog(null, msg);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Vector<Question> reqQuestion(Register register) {
        Vector<Question> vtQuestion = null;
        try {
            byte[] packetSend = convert.allToPacket(ACTION[6], register);
            os.write(packetSend);

            byte[] packetReceive = new byte[4096];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[7])) {
                    vtQuestion = (Vector<Question>) convert.byteToObject(data);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vtQuestion;
    }

    public int reqSubmit(Vector<String> vtAnswer) {
        int total = 0;
        try {
            byte[] packetSend = convert.allToPacket(ACTION[8], vtAnswer);
            os.write(packetSend);

            byte[] packetReceive = new byte[1024];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[9])) {
                    total = (int) convert.byteToObject(data);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public Vector<History> reqHistory(String username) {
        Vector<History> vtHistory = null;
        try {
            byte[] packetSend = convert.allToPacket(ACTION[10], username);
            os.write(packetSend);

            byte[] packetReceive = new byte[2048];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[11])) {
                    vtHistory = (Vector<History>) convert.byteToObject(data);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vtHistory;
    }

    public Vector<DetailHistory> showDetail(int idTask) {
        Vector<DetailHistory> vt = null;
        try {
            byte[] packetSend = convert.allToPacket(ACTION[12], (Integer) idTask);
            System.out.println(packetSend.length);
           os.write(packetSend);
            
            byte[] packetReceive = new byte[4096];
            if (is.read(packetReceive) > 0) {
                packetToAll(packetReceive);
                if (action.equals(ACTION[12])) {
                    vt = (Vector<DetailHistory>) convert.byteToObject(data);
                    System.out.println(vt.firstElement().getIdQuestion());
                    return vt;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //public Vector<Question> 
    public void packetToAll(byte[] bytes) {
        int i = 0;
        action = "";
        data = null;
        byte[] length = new byte[4];
        for (i = 0; i < 4; i++) {
            action += (char) bytes[i];
        }
        System.arraycopy(bytes, 4, length, 0, 4);
        data_length = convert.byteToInt(length);
        data = new byte[data_length];
        System.arraycopy(bytes, 8, data, 0, data_length);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        System.out.println(client.isOnline());
        client.connect();
        System.out.println(client.isOnline());
    }
}
