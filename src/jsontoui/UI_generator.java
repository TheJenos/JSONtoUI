/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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

    public UI_generator(String JSON, HashMap<String, String> data) {
        if (data != null) {
            this.paras = data;
        }
        obj = new JSONObject(JSON);
        JSONArray arr = obj.getJSONArray("UI");
        int width = obj.getInt("width") + 15;
        int height = obj.getInt("height") + 25;
        boolean packed = obj.getBoolean("packed");
        String caption = obj.getString("text");
        setTitle(caption);
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(jp);
        jp.setBounds(0, 0, width, height);
        addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                resizeall();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }

        });
        for (int i = 0; i < arr.length(); i++) {
            String type_ui = arr.getJSONObject(i).getString("type");
            int x = arr.getJSONObject(i).getInt("x");
            int y = arr.getJSONObject(i).getInt("y");
            int z = arr.getJSONObject(i).getInt("z");
            int uwidth = arr.getJSONObject(i).getInt("width");
            int uheight = arr.getJSONObject(i).getInt("height");
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
                String onclick = arr.getJSONObject(i).getString("onclick");
                text = replace_paras(data, text);
                JButton jl = new JButton(text);
                jl.setBounds(x, y, uwidth, uheight);
                jp.add(jl);
                //jp.setComponentZOrder(jl, z);
                Zindex.put(jl, arr.getJSONObject(i));
                jl.addActionListener(new onclick(onclick) {
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
                jt.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        JTextField jt = (JTextField) evt.getComponent();
                        paras.put(text.substring(1), jt.getText());
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
        setVisible(true);
        //setIndex();
    }

    private Runnable Indexing = new Runnable() {
        @Override
        public void run() {
            while (update) {
                setIndex();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UI_generator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    void resizeall(){
        int width = obj.getInt("width") + 15;
        int height = obj.getInt("height") + 25;
        int rwidth = getWidth() + 15;
        int rheight = getHeight() + 26;
        Component all[] = jp.getComponents();
        for (Component component : all) {
           JSONObject cpm = Zindex.get(component);
           int newx = (cpm.getInt("x")*rwidth)/width;
           int newy = (cpm.getInt("y")*rheight)/height;
           int newwidth = (cpm.getInt("width")*rwidth)/width;
           int newheight = (cpm.getInt("width")*rheight)/height;
           component.setLocation(newx, newy);
           component.setSize(newwidth, newheight);
        }
        jp.setBounds(0,0,getWidth()+15, getHeight()+25);
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
        String json = "{\"text\":\"Testing\",\"height\":458,\"packed\":false,\"width\":433,\"UI\":[{\"height\":122,\"width\":141,\"path\":\"C:\\\\Users\\\\Thanura\\\\Documents\\\\12038711_937872686274051_6084992746435062302_o.jpg\",\"onclick\":\"\",\"type\":\"Image\",\"z\":0,\"y\":20,\"x\":279},{\"height\":456,\"width\":433,\"path\":\"C:\\\\Users\\\\Thanura\\\\Documents\\\\12038711_937872686274051_6084992746435062302_o.jpg\",\"onclick\":\"\",\"type\":\"Image\",\"z\":1,\"y\":2,\"x\":0}]}";
        UI_generator ui = new UI_generator(json, null);
    }
}
