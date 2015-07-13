/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import ProcessSocket.Client;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Huy Hùng
 * @Copyright (c) 2015 Huy Hùng
 */
public class Report {

    private int idTask;
    private String pName;
    private String pSubject;
    private String level;
    private String pTime;
    private int total;
    private Client client;

    public Report(Client client, int idTask, String pName, String pSubject, String level, String pTime, int total) {
        this.client = client;
        this.idTask = idTask;
        this.pName = pName;
        this.pSubject = pSubject;
        this.level = level;
        this.pTime = pTime;
        this.total = total;
    }

    public void createReport() {
        try {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("pName", pName);
            parameters.put("pSubject", pSubject);
            parameters.put("pTime", pTime);
            parameters.put("pTotal", total);
            parameters.put("pLevel", level);

            String current = new java.io.File(".").getCanonicalPath();
            File f = new File(current + "/lib/reports.jasper");
            JasperReport jr = (JasperReport) JRLoader.loadObject(f);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, new JRBeanCollectionDataSource(client.showDetail(idTask)));
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
