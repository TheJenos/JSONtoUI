/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class UI_generator extends JFrame {

    HashMap<Component, JSONObject> Zindex = new HashMap<Component, JSONObject>();
    HashMap<String, String> paras = new HashMap<String, String>();
    JSONObject obj = new JSONObject();

    Boolean update = true;
    JLayeredPane jp = new JLayeredPane();

    class Ke extends KeyAdapter {

        String ss = "";

        public Ke(String ss) {
            this.ss = ss;
        }
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
            }
        }
        if (packed) {
            pack();
        }

        //pack();
        setVisible(
                true);
        new Thread(Indexing).start();
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
                //System.out.println(paras);
                //System.out.println(data);
                switch (command.toLowerCase()) {
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
