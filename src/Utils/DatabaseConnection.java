/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import jsontoui.StaticVal;

/**
 *
 * @author AssHunter
 */
public class DatabaseConnection {

    private static Connection c;
    private static HashMap querys = new HashMap();
    public static boolean c1_stat = true;
    public static boolean c2_stat = true;
    public static boolean loop = true;

    public static int getResultSetCount(ResultSet r) throws Exception {
        int i = 0;
        while (r.next()) {
            i++;
        }
        r.beforeFirst();
        return i;
    }

    public static Vector[] hashmapToSets(LinkedHashMap dataline) {
        Vector data[] = new Vector[2];
        data[0] = new Vector();
        data[1] = new Vector();
        Iterator i = dataline.keySet().iterator();
        while (i.hasNext()) {
            data[0].add(i.next());
        }
        i = dataline.values().iterator();
        while (i.hasNext()) {
            data[1].add(i.next());
        }
        return data;
    }

    public static String getSmallData(String table, String column, String where) throws Exception {
        String sql = "SELECT " + column + " FROM " + table + " WHERE " + where;
        ResultSet rs = search(sql);
        if (rs.next()) {
            return rs.getString(column);
        } else {
            throw new NullPointerException();
        }
    }

    public static HashMap getDataRow(String table, Vector column, String where) throws Exception {
        Vector row = column;
        String rows = "";
        for (int i = 0; i < row.size(); i++) {
            rows += "`" + Utils.SimpleEncrypt(row.get(i).toString()) + "`";
            rows += (i == row.size() - 1) ? "" : ",";
        }
        String sql = "SELECT " + rows + " FROM " + table + " WHERE " + where;
        ResultSet rs = search(sql);
        if (rs.next()) {
            HashMap hm = new HashMap();
            for (int i = 0; i < row.size(); i++) {
               hm.put(row.get(i).toString(), rs.getString(row.get(i).toString()));
            }
            return hm;
        } else {
            throw new NullPointerException();
        }
    }

    private static void setConnection1() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        try {
            c = DriverManager.getConnection("jdbc:mysql://" + StaticVal.Host + ":" + StaticVal.Port + "/" + StaticVal.DB + "?zeroDateTimeBehavior=convertToNull", StaticVal.User, StaticVal.Pass);
        } catch (Exception e) {
        }
    }
    
    public static void iud(String Sql) throws Exception {
        if (c == null) setConnection1();
        c.createStatement().executeUpdate(Sql);
    }

    public static ResultSet search(String sql) throws Exception {
        if (c == null)setConnection1();
        return c.createStatement().executeQuery(sql);
    }

    public static Connection getConnection() throws Exception {
        if (c == null)setConnection1();
        return c;
    }


    public static int getRowCount(String table, String WHERE) throws Exception {
        int c = 1;
        String sql = "SELECT * FROM `" + Utils.SimpleEncrypt(table) + "` WHERE " + WHERE;
        System.out.println(sql);
        ResultSet rs = search(sql);
        while (rs.next()) {
            c++;
        }
        return c;

    }

    public static int getNextNumber(String table) throws Exception {
        int c = 1;
        String sql = "SHOW TABLE STATUS WHERE `name`='" + table + "'";
        System.out.println(sql);
        ResultSet rs = search(sql);
        rs.next();
        return rs.getInt("Auto_increment");

    }

    public static void Insert(String table, LinkedHashMap<String, String> dataline) throws Exception {
        Vector dataset[] = hashmapToSets(dataline);
        Vector row = dataset[0];
        Vector data = dataset[1];
        String rows = "";
        for (int i = 0; i < row.size(); i++) {
            rows += "`" + Utils.SimpleEncrypt(row.get(i).toString()) + "`";
            rows += (i == row.size() - 1) ? "" : ",";
        }
        String datas = "";
        for (int i = 0; i < data.size(); i++) {
            datas += "'" + Utils.SimpleEncrypt(data.get(i).toString()) + "'";
            datas += (i == data.size() - 1) ? "" : ",";
        }
        String sql = "INSERT INTO `" + Utils.SimpleEncrypt(table) + "` (" + rows + ") VALUES (" + datas + ")";
        System.out.println(sql);
        iud(sql);
    }

    public static void Update(String table, LinkedHashMap<String, String> dataline, String WHERE) throws Exception {
        Vector dataset[] = hashmapToSets(dataline);
        Vector row = dataset[0];
        Vector data = dataset[1];
        String rows = "";
        for (int i = 0; i < row.size(); i++) {
            rows += "`" + Utils.SimpleEncrypt(row.get(i).toString()) + "`='" + Utils.SimpleEncrypt(data.get(i).toString()) + "'";
            rows += (i == row.size() - 1) ? "" : ",";
        }
        String sql = "UPDATE `" + Utils.SimpleEncrypt(table) + "` SET " + rows + " WHERE " + WHERE;
        System.out.println(sql);
        iud(sql);
    }

    public static void Delete(String table, String WHERE) throws Exception {
        String sql = "DELETE FROM `" + Utils.SimpleEncrypt(table) + "` WHERE " + WHERE;
        System.out.println(sql);
        iud(sql);
    }

}
