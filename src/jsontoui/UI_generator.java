/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import Utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class UI_generator extends JFrame {

    HashMap<Component, JSONObject> Zindex = new HashMap<Component, JSONObject>();
    HashMap<String, String> paras = new HashMap<String, String>();
    HashMap<String, String> oldparas = new HashMap<String, String>();

    JSONObject obj = new JSONObject();
    JSONObject Config_obj;
    Connection c = null;

    @Override
    public void dispose() {
        binds.stop();
        binds = null;
        super.dispose(); //To change body of generated methods, choose Tools | Templates.
    }

    Boolean update = true;
    JLayeredPane jp = new JLayeredPane();
    Timer binds = new Timer(5, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (Map.Entry<String, String> entry : paras.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (oldparas.get(key) == null || !oldparas.get(key).equals(value)) {
                    oldparas.put(key, value);
                    for (Map.Entry<Component, JSONObject> entry1 : Zindex.entrySet()) {
                        Component key1 = entry1.getKey();
                        JSONObject value1 = entry1.getValue();
                        String type_ui = value1.getString("type");
                        if (type_ui.toLowerCase().equals("lable") && isThere(value1.getString("text"), key)) {
                            String text = value1.getString("text");
                            text = replace_paras(paras, text);
                            JLabel jl = (JLabel) key1;
                            jl.setText(text);
                        } else if (type_ui.toLowerCase().equals("button") && isThere(value1.getString("text"), key)) {
                            String text = value1.getString("text");
                            text = replace_paras(paras, text);
                            JButton jl = (JButton) key1;
                            jl.setText(text);
                        } else if (type_ui.toLowerCase().equals("edittext") && isThere(value1.getString("text"), key)) {
                            JTextField jl = (JTextField) key1;
                            String text = value1.getString("text");
                            text = replace_paras(paras, text);
                            jl.setText(text);
                        } else if (type_ui.toLowerCase().equals("image") && isThere(value1.getString("path"), key)) {
                            SimpleImageView jl = (SimpleImageView) key1;
                            String text = value1.getString("path");
                            text = replace_paras(paras, text);
                            jl.loadImageNOThread(new File(text));
                        } else if (type_ui.toLowerCase().equals("sql_table") && (isThere(value1.getJSONObject("data").getString("lable"), key) || isThere(value1.getString("text"), key))) {
                            String text = value1.getString("text");
                            JSONObject cdata = value1.getJSONObject("data");
                            String text2 = cdata.getString("lable");
                            text = replace_paras(paras, text);
                            text2 = replace_paras(paras, text2);
                            SQL_Table jl = (SQL_Table) key1;
                            jl.setSQL(text);
                            jl.setLable(text2);
                            jl.refreash();
                        }
                    }
                }
            }
        }
    });

    public void Refreash() {
        for (Map.Entry<Component, JSONObject> entry1 : Zindex.entrySet()) {
            Component key1 = entry1.getKey();
            JSONObject value1 = entry1.getValue();
            String type_ui = value1.getString("type");
            if (type_ui.toLowerCase().equals("image")) {
                SimpleImageView jl = (SimpleImageView) key1;
                String text = value1.getString("path");
                text = replace_paras(paras, text);
                jl.loadImageNOThread(new File(text));
            } else if (type_ui.toLowerCase().equals("sql_table")) {
                String text = value1.getString("text");
                JSONObject cdata = value1.getJSONObject("data");
                String text2 = cdata.getString("lable");
                text = replace_paras(paras, text);
                text2 = replace_paras(paras, text2);
                SQL_Table jl = (SQL_Table) key1;
                jl.setSQL(text);
                jl.setLable(text2);
                jl.refreash();
            }
        }
    }

    public boolean isThere(String s1, String s2) {
        return !s1.replaceAll(s2, "").equals(s1);
    }

    private void clearit(String data) {
        for (Map.Entry<Component, JSONObject> entry1 : Zindex.entrySet()) {
            Component key1 = entry1.getKey();
            JSONObject value1 = entry1.getValue();
            String type_ui = value1.getString("type");
            if (type_ui.toLowerCase().equals("edittext") && value1.getString("text").equals(data)) {
                JTextField jl = (JTextField) key1;
                jl.setText("");
                System.out.println(paras);
                paras.put(data.substring(1), "");
            }
        }
    }

    class Ke extends KeyAdapter {

        String ss = "";

        public Ke(String ss) {
            this.ss = ss;
        }
    }

    public void iud(String Sql) throws Exception {
        if (c == null) {
            connectToDataBase();
        }
        System.out.println(Sql);
        c.createStatement().executeUpdate(Sql);
    }

    public Connection getConnection() {
        try {
            if (c == null) {
                connectToDataBase();
            }
        } catch (Exception e) {
        }
        return c;
    }

    public void connectToDataBase() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        c = DriverManager.getConnection("jdbc:mysql://" + Config_obj.get("Host").toString() + ":" + Config_obj.get("Port").toString() + "/" + Config_obj.get("DataBase Name").toString() + "?zeroDateTimeBehavior=convertToNull", Config_obj.get("Root User").toString(), Utils.decrypt(Config_obj.get("Root Password").toString()));
    }

    public UI_generator(String JSON, HashMap<String, String> data) {
        if (data != null) {
            this.paras = data;
        }
        obj = new JSONObject(JSON);
        JSONArray arr = obj.getJSONArray("UI");
        int width = obj.getInt("width") + 0;
        int height = obj.getInt("height") + 0;
        boolean packed = obj.getBoolean("packed");
        String caption = obj.getString("text");
        Config_obj = new JSONUI_File(obj.getString("config")).getJSON_Object();
        setTitle(caption);
        setSize(width + 16, height + 39);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(jp);
        jp.setBounds(0, 0, width, height);
        jp.setOpaque(true);
        jp.setBackground(Color.WHITE);
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                resizeall();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                resizeall();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                resizeall();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                resizeall();
            }

        });
        for (int i = 0; i < arr.length(); i++) {
            String type_ui = arr.getJSONObject(i).getString("type");
            int x = arr.getJSONObject(i).getInt("x");
            int y = arr.getJSONObject(i).getInt("y");
            int z = arr.getJSONObject(i).getInt("z");
            int uwidth = arr.getJSONObject(i).getInt("width");
            int uheight = arr.getJSONObject(i).getInt("height");
            JSONObject cdata = arr.getJSONObject(i).getJSONObject("data");
            if (type_ui.toLowerCase().equals("lable")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JLabel jl = new JLabel(text);
                jp.add(jl);
                jl.setBounds(x, y, uwidth, uheight);
                //jp.setComponentZOrder(jl, z);
                Zindex.put(jl, arr.getJSONObject(i));
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.setSize(uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("button")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JButton jl = new JButton();
                jl.setOpaque(true);
                jl.setBounds(x, y, uwidth, uheight);
                jl.setText(text);
                jp.add(jl);
                //jp.setComponentZOrder(jl, z);
                Zindex.put(jl, arr.getJSONObject(i));
                jl.addActionListener(new onclick(cdata.getString("onclick")) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onclick(script);
                    }
                });
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.setSize(uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("edittext")) {
                final String text = arr.getJSONObject(i).getString("text");
                JTextField jt = new JTextField();
                paras.put(text.substring(1), "");
                jt.addKeyListener(new Ke(text) {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        JTextField jt = (JTextField) evt.getComponent();
                        paras.put(ss.substring(1), jt.getText());
                    }
                });
                jp.add(jt);
                //jp.setComponentZOrder(jt, z);
                Zindex.put(jt, arr.getJSONObject(i));
                jt.setPreferredSize(new Dimension(uwidth, uheight));
                jt.setSize(uwidth, uheight);
                jt.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("image")) {
                String text = arr.getJSONObject(i).getString("path");
                text = replace_paras(data, text);
                SimpleImageView img = new SimpleImageView();
                jp.add(img);
                //jp.setComponentZOrder(img, z);
                Zindex.put(img, arr.getJSONObject(i));
                img.setPreferredSize(new Dimension(uwidth, uheight));
                img.setSize(uwidth, uheight);
                img.loadImageNOThread(new File(text));
                img.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("sql_table")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JSONObject cols = cdata.getJSONObject("columns");
                SQL_Table img = new SQL_Table(text, getConnection(), Utils.JSONtoHashMap(cols));
                img.setLable(cdata.getString("lable"));
                img.refreash();
                jp.add(img);
                Zindex.put(img, arr.getJSONObject(i));
                img.setPreferredSize(new Dimension(uwidth, uheight));
                img.setSize(uwidth, uheight);
                img.setBounds(x, y, uwidth, uheight);
            }
        }
        if (packed) {
            pack();
        }
        setVisible(true);
        new Thread(Indexing).start();
        binds.start();
    }

    private Runnable Indexing = new Runnable() {
        @Override
        public void run() {
            while (update) {
                System.gc();
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(UI_generator.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    void resizeall() {
        int width = obj.getInt("width");
        int height = obj.getInt("height");
        int rwidth = jp.getWidth();
        int rheight = jp.getHeight();
        Component all[] = jp.getComponents();
        for (Component component : all) {
            JSONObject cpm = Zindex.get(component);
            Double oldwidthgap = width - cpm.getDouble("width") - cpm.getDouble("x");
            Double newwidth = rwidth - oldwidthgap - cpm.getDouble("x");
            Double oldheightgap = height - cpm.getDouble("height") - cpm.getDouble("y");
            Double newheight = rheight - oldheightgap - cpm.getDouble("y");
            Double rightsidegap = width - cpm.getDouble("width") - cpm.getDouble("x");
            Double leftsidegap = cpm.getDouble("x");
            Double topsidegap = cpm.getDouble("y");
            Double bottomsidegap = height - cpm.getDouble("height") - cpm.getDouble("y");
            if (!cpm.getJSONObject("data").getBoolean("resize")) {
                if (leftsidegap > rightsidegap) {
                    component.setLocation((rwidth - width) + cpm.getInt("x"), component.getY());
                }
                if (topsidegap > bottomsidegap) {
                    component.setLocation(component.getX(), (rheight - height) + cpm.getInt("y"));
                }

            }
            if (cpm.getJSONObject("data").getBoolean("resize")) {
                if (leftsidegap + rightsidegap > topsidegap + bottomsidegap) {
                    component.setSize(component.getWidth(), newheight.intValue());
                } else {
                    component.setSize(newwidth.intValue(), component.getHeight());
                }
            }
            jp.setComponentZOrder(component, cpm.getInt("z"));
        }
        jp.setBounds(0, 0, getWidth() - 16, getHeight() - 39);
    }

    void setIndex() {
        for (Map.Entry<Component, JSONObject> e : Zindex.entrySet()) {
            Component key = e.getKey();
            JSONObject value = e.getValue();
            //jp.setComponentZOrder(key, value);
        }
    }

    String replace_paras(HashMap<String, String> data, String s) {
        if (data == null) {
            return s;
        }
        for (Map.Entry<String, String> e : data.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            s = s.replaceAll("@" + key, Matcher.quoteReplacement(value));
        }
        return s;
    }

    void onclick(String scripts) {
        String script_lines[] = scripts.split("\n");
        for (String script : script_lines) {
            String command = script.split("=")[0];
            if (script.split("=").length > 1) {
                String data = script.split("=")[1];
                data = replace_paras(paras, data);
                switch (command.toLowerCase()) {
                    case "clear":
                        clearit(script.split("=")[1]);
                        break;
                    case "refreash":
                        Refreash();
                        break;
                    case "val":
                        String atrib[] = script.split("=")[1].split(",");
                        paras.put(atrib[0].substring(1), (atrib.length > 1) ? atrib[1]:"");
                        break;
                    case "sql":
                        try {
                            iud(data);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "open":
                        JSONUI_File jf = new JSONUI_File(data);
                        UI_generator ui = new UI_generator(jf.getJSON(), paras);
                        break;
                    case "close":
                        this.dispose();
                        break;
                    case "alert":
                        String atribs[] = data.split(",");
                        if (atribs.length > 1) {
                            switch (atribs[0].toLowerCase()) {
                                case "error":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1], JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "warn":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1], JOptionPane.WARNING_MESSAGE);
                                    break;
                                case "info":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1], JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, data);
                        }
                        break;
                }
            }
        }
    }

    public static void main(String args[]) {
        String json = "{\"text\":\"Testing\",\"height\":492,\"packed\":false,\"width\":457,\"UI\":[{\"text\":\"#ff0033,Close\",\"height\":50,\"width\":80,\"data\":{\"onclick\":\"close=this\",\"resize\":true},\"type\":\"Button\",\"z\":0,\"y\":442,\"x\":377},{\"text\":\"Hey,@name\",\"height\":76,\"width\":431,\"data\":{\"onclick\":\"\",\"resize\":false},\"type\":\"EditText\",\"z\":1,\"y\":7,\"x\":14}]}";
        UI_generator ui = new UI_generator(json, null);
    }
}
