/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessSocket;

import Account.AccountDAO;
import Entity.Account;
import Work.Convert;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class ClientThread extends Thread {

    private InputStream is = null;
    private OutputStream os = null;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientsCount;

    private final String[] ACTION = {
      // Exit; Message; Login; logout; register acc; change password; 
        "EXIT", "MESG", "LGIN", "LOUT", "RACC", "CPWD",
      // Req Question, Res Question, Req Result, Res Result
        "RQQU", "RSQU", "RQRS", "RSRS", 
     // Req, Res History, Detail
        "RQHI", "RSHI", "RQDT"}; 

    private String action;
    private int data_length;
    private byte[] data = null;

    private Convert convert = new Convert();
    private ProcessAction process = new ProcessAction();
    private AccountDAO accDAO = new AccountDAO();
    private boolean isStoped = true;

    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] threads = this.threads;
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            while (isStoped) {
                byte[] packetReceive = new byte[1024];
                if (is.read(packetReceive) > 0) {
                    System.out.println("Received...");
                    packetToAll(packetReceive);

                    // Xử lý các yêu cầu từ client                    
                    byte[] packetSend = null;
                    if (action.equals(ACTION[0])) {           // Exit
                        close();
                    } else if (action.equals(ACTION[1])) {     // Message
//                        packetSend = process.logIn(data);
                    } else if (action.equals(ACTION[2])) {     // Login
                        packetSend = process.logIn(data);
                    } else if (action.equals(ACTION[3])) {     // Logout
                        process.logOut(data);
                    } else if (action.equals(ACTION[4])) {     // Register Account
                        packetSend = process.registerAcc(data);
                    } else if (action.equals(ACTION[5])) {     // Change pasword
                        packetSend = process.changePWD(data);
                    } else if (action.equals(ACTION[6])) {     // Request Question
                        packetSend = process.registerQuesstion(data);
                    } else if (action.equals(ACTION[8])) {     // Request Result
                        packetSend = process.reqSubmit(data);
                    } else if (action.equals(ACTION[10])) {    // Request History
                        packetSend = process.reqHistory(data);
                    } else if (action.equals(ACTION[12])) {    // Request show detail
                        packetSend = process.reqShowDetail(data);
                    }
                    if (packetSend != null) {
                        os.write(packetSend);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        // Delete thread

    }

    public void login(byte[] bytes) {

    }

    public synchronized void close() {
        isStoped = false;
        synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                    break;
                }
            }
        }
        try {
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] allToPacket(String action, Object obj) throws Exception {
        // action to byte array
        byte[] action_code = action.getBytes();
        // Object to byte array
        data = convert.objectToByte(obj);
        byte[] length = convert.intToByte(data.length);
        // All = [action_code (4 byte), data_length(4 byte), data]
        byte[] all = new byte[8 + data.length];

        System.arraycopy(action_code, 0, all, 0, 4);
        System.arraycopy(length, 0, all, 4, 4);
        System.arraycopy(data, 0, all, 8, data.length);
        return all;
    }

    public void packetToAll(byte[] bytes) {
        action = "";
        byte[] length = new byte[4];
        // Get action
        for (int i = 0; i < 4; i++) {
            action += (char) bytes[i];
        }
        //Get data_length
        System.arraycopy(bytes, 4, length, 0, 4);
        data_length = convert.byteToInt(length);
        // Get data
        data = new byte[data_length];
        try {
            System.arraycopy(bytes, 8, data, 0, data_length);
        } catch (Exception ex) {
            System.out.println("Here!");
        }
    }
}
