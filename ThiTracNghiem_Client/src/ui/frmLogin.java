/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ProcessSocket.Client;
import Entity.Account;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HuyHung
 */
public class frmLogin extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    private int iCount = 0;
    private Client client = new Client();

    private String title = "Chào mừng tới trang đăng nhập!";
    private TitleRun vt = new TitleRun(title);

    public frmLogin() {
        vt.start();
        initComponents();
        setLocation(400, 150);
        setResizable(false);
        loadRemember();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        tfUsername = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        pPassword = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        cbRemember = new javax.swing.JCheckBox();
        btLogin = new javax.swing.JButton();
        btRegister = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setLayout(new java.awt.GridLayout(3, 0));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("<html>MANAGA SYSTEM<br></br>\n<p align =\"center\" >LOGIN</p>");
        jPanel4.add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        jLabel3.setText("Username");
        jPanel1.add(jLabel3);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Admin", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM));

        tfUsername.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        tfUsername.setPreferredSize(new java.awt.Dimension(150, 30));
        tfUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfUsernameActionPerformed(evt);
            }
        });
        tfUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfUsernameFocusGained(evt);
            }
        });
        jPanel5.add(tfUsername);

        jPanel1.add(jPanel5);

        jPanel4.add(jPanel1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/key.png"))); // NOI18N
        jLabel4.setText("Password");
        jPanel2.add(jLabel4);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "admin", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM));

        pPassword.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        pPassword.setPreferredSize(new java.awt.Dimension(150, 30));
        pPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pPasswordActionPerformed(evt);
            }
        });
        pPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pPasswordFocusGained(evt);
            }
        });
        jPanel6.add(pPassword);

        jPanel2.add(jPanel6);

        jPanel4.add(jPanel2);

        cbRemember.setText("Remember");

        btLogin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/locked.png"))); // NOI18N
        btLogin.setText("Đăng nhập");
        btLogin.setMargin(new java.awt.Insets(2, 7, 2, 14));
        btLogin.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/locked.png"))); // NOI18N
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        btRegister.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btRegister.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/new.png"))); // NOI18N
        btRegister.setText("Đăng ký ");
        btRegister.setMargin(new java.awt.Insets(2, 7, 2, 14));
        btRegister.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/locked.png"))); // NOI18N
        btRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbRemember)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btRegister)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(cbRemember)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btLogin)
                    .addComponent(btRegister)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfUsernameActionPerformed
        pPassword.requestFocus();
    }//GEN-LAST:event_tfUsernameActionPerformed

    private void tfUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUsernameFocusGained
        tfUsername.selectAll();
    }//GEN-LAST:event_tfUsernameFocusGained

    private void pPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pPasswordActionPerformed
        btLogin.doClick();
    }//GEN-LAST:event_pPasswordActionPerformed

    private void pPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pPasswordFocusGained
        pPassword.selectAll();
    }//GEN-LAST:event_pPasswordFocusGained

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        clickLogin();
    }//GEN-LAST:event_btLoginActionPerformed

    private void btRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRegisterActionPerformed
        clickRegister();
    }//GEN-LAST:event_btRegisterActionPerformed

    private void loadRemember() {
        try {
            if (new File("account.dat").exists()) {
                FileInputStream fis = new FileInputStream("account.dat");
                ObjectInputStream ois = new ObjectInputStream(fis);
                Account acc = (Account) ois.readObject();
                tfUsername.setText(acc.getUsername());
                pPassword.setText(acc.getPassword());
                ois.close();
                fis.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FIle not found");
        } catch (IOException ex) {
            Logger.getLogger(frmLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(frmLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void remember(boolean check) {
        try {
            if (check) {
                FileOutputStream fos = new FileOutputStream("account.dat");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                Account acc = new Account(tfUsername.getText(), String.valueOf(pPassword.getPassword()));
                oos.writeObject(acc);
                oos.close();
                fos.close();
            }else{
                new File("account.dat").delete();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(frmLogin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(frmLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Click button login
    private void clickLogin() {
        String user = tfUsername.getText();
        String pass = String.valueOf(pPassword.getPassword());
        if (user.equalsIgnoreCase("") || pass.equals("")) {
            JOptionPane.showMessageDialog(this, "   Login fail!.\n   You need enter username, password.", "   LOGIN   ", 0);
            iCount++;
        } else {
            System.out.println("Status now: " + client.isOnline());

            if (!client.isOnline()) {
                client.connect();
                System.out.println("Status1: " + client.isOnline());
            }
            Account acc = new Account(user, pass);
            Account resAcc = client.reqLogin(acc);
            if (resAcc != null) {
                remember(cbRemember.isSelected());
                this.dispose();
                new frmMain(client, resAcc).setVisible(true);
            }

        }

    }

    private void clickRegister() {
        new frmRegister().setVisible(true);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmLogin().setVisible(true);
            }
        });
    }

    class TitleRun extends Thread {

        private volatile boolean isRunning = true;
        private String arrText;

        public TitleRun(String title) {
            arrText = title;
        }

        public void run() {
            String tmp = "";
            int iCount = -1;
            while (isRunning) {
                try {
                    iCount++;
                    if (iCount == arrText.length()) {
                        iCount = 0;
                        tmp = "";
                    }
                    tmp += arrText.charAt(iCount);
                    setTitle(tmp);
                    sleep(250);
                } catch (InterruptedException ex) {
                }
            }
        }

        public void kill() {
            isRunning = false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btLogin;
    private javax.swing.JButton btRegister;
    private javax.swing.JCheckBox cbRemember;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField pPassword;
    private javax.swing.JTextField tfUsername;
    // End of variables declaration//GEN-END:variables
}