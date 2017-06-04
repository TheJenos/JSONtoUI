/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Thanura
 */
public class SQL_Table extends javax.swing.JPanel {

    public String SQL;
    public Connection con;
    public HashMap<String, String> Columns;
    public DefaultTableModel dtm;

    public String getSQL() {
        return SQL;
    }

    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public HashMap<String, String> getColumns() {
        return Columns;
    }

    public void setColumns(HashMap<String, String> Colums) {
        this.Columns = Colums;
    }

    /**
     * Creates new form SQL_Table
     */
    public SQL_Table() {
        initComponents();
    }

    public SQL_Table(String SQL, Connection con, HashMap<String, String> Colums) {
        this.SQL = SQL;
        this.con = con;
        this.Columns = Colums;
        initComponents();
        dtm = (DefaultTableModel) jTable1.getModel();
    }

    public void updatecols() {
        dtm.setColumnCount(0);
        for (Map.Entry<String, String> entry : Columns.entrySet()) {
            String value = entry.getValue();
            dtm.addColumn(value);
        }
    }

    public void refreash() {
        try {
            dtm.setRowCount(0);
            dtm.setColumnCount(0);
            ResultSet rs = con.createStatement().executeQuery(SQL);
            for (Map.Entry<String, String> entry : Columns.entrySet()) {
                String value = entry.getValue();
                dtm.addColumn(value);
            }
            while (rs.next()) {
                Vector<String> v = new Vector<String>();
                for (Map.Entry<String, String> entry : Columns.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    v.add(rs.getString(key));
                }
                dtm.addRow(v);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setLable(String s) {
        jLabel1.setText(s);
    }

    public String getLable() {
        return jLabel1.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}