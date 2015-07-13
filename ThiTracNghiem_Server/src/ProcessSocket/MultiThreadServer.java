/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessSocket;

import Work.Convert;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class MultiThreadServer implements Runnable {

    //Create client, server socket
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;

    // Maximum Client can be connected to server
    private static final int MAX_CLIENT = 2;
    private static ClientThread[] threads = new ClientThread[MAX_CLIENT];

    private boolean isStopped = true;
    private int port;

    public MultiThreadServer(int port) {
        this.port = port;
    }

    public void turnOnServer() {
        port = 1994;
        isStopped = true;
        try {
            //Create socket server
            serverSocket = new ServerSocket(port);
            System.out.println("Server is ready...");
        } catch (IOException ex) {
            System.out.println("Create socket server fail: \n" + ex.toString());
        }
    }

    public synchronized void turnOffServer() {
        for (int i = 0; i < MAX_CLIENT; i++) {
            if (threads[i] != null) {
                threads[i].close();
            }
        }
        isStopped = false;
        try {
            serverSocket.close();
            System.out.println("Server is offline");
        } catch (IOException ex) {
            Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        turnOnServer();
        while (isStopped) {
            try {
                clientSocket = serverSocket.accept();
                OutputStream os = clientSocket.getOutputStream();;
                System.out.println(clientSocket.getInetAddress());
                int i = 0;
                for (i = 0; i < MAX_CLIENT; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        os.write(new Convert().allToPacket("MESG", "Kết nối thành công."));
                        System.out.println("Một client kết nối. Index: " + (i + 1));
                        break;
                    }
                }
                if (i == MAX_CLIENT) {
                    os.write(new Convert().allToPacket("EXIT", "Server too busy. Try later."));
                    clientSocket.close();
                }
                System.out.println("Total Thread: " + (i + 1));
            } catch (IOException ex) {
                System.out.println("Create connection fail: \n" + ex.toString());
            } catch (Exception ex) {
                Logger.getLogger(MultiThreadServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
