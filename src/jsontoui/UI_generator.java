/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class UI_generator extends JFrame {

    HashMap<Component,Integer> Zindex = new HashMap<Component, Integer>();
    
    public UI_generator(String JSON, HashMap<String, String> data) {
        JSONObject obj = new JSONObject(JSON);
        JSONArray arr = obj.getJSONArray("UI");
        int width = obj.getInt("width") + 15;
        int height = obj.getInt("height") + 25;
        boolean packed = obj.getBoolean("packed");
        String caption = obj.getString("text");
        setTitle(caption);
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
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
                getContentPane().add(jl , new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, -1, -1));
                getContentPane().setComponentZOrder(jl, z);
                Zindex.put(jl, z);
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.setSize(uwidth, uheight);
                //jl.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("button")) {
                String text = arr.getJSONObject(i).getString("text");
                String onclick = arr.getJSONObject(i).getString("onclick");
                text = replace_paras(data, text);
                JButton jl = new JButton(text);
                getContentPane().add(jl, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, -1, -1));
                //getContentPane().setComponentZOrder(jl, z);
                Zindex.put(jl, z);
                jl.addActionListener(new onclick(onclick) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onclick(script);
                    }
                });
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.setSize(uwidth, uheight);
                //jl.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("edittext")) {
                String text = arr.getJSONObject(i).getString("text");
                text = replace_paras(data, text);
                JTextField jt = new JTextField(text);
                getContentPane().add(jt,new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, -1, -1));
                //getContentPane().setComponentZOrder(jt, z);
                Zindex.put(jt, z);
                jt.setPreferredSize(new Dimension(uwidth, uheight));
                jt.setSize(uwidth, uheight);
                //jt.setBounds(x, y, uwidth, uheight);
            } else if (type_ui.toLowerCase().equals("image")) {
                String text = arr.getJSONObject(i).getString("path");
                text = replace_paras(data, text);
                SimpleImageView img = new SimpleImageView();
                getContentPane().add(img,new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, -1, -1));
                //getContentPane().setComponentZOrder(img, z);
                Zindex.put(img, z);
                img.setPreferredSize(new Dimension(uwidth, uheight));
                img.setSize(uwidth, uheight);
                img.loadImageNOThread(new File(text));
                //img.setBounds(x, y, uwidth, uheight);
            }
        }
        if (packed) {
            pack();
        }
        setVisible(true);
        setIndex();
    }

    void setIndex(){
        for (Map.Entry<Component, Integer> e : Zindex.entrySet()) {
            Component key = e.getKey();
            int value = e.getValue();
            getContentPane().setComponentZOrder(key, value);
        }
    }
    
    String replace_paras(HashMap<String, String> data, String s) {
        if (data == null) {
            return s;
        }
        for (Map.Entry<String, String> e : data.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            s = s.replaceAll("@" + key, value);
        }
        return s;
    }

    
    
    void onclick(String scripts) {
        String script_lines[] = scripts.split("\n");
        for (String script : script_lines) {
            String command = script.split("=")[0];
            if (script.split("=").length > 1) {
                String data = script.split("=")[1];
                switch (command.toLowerCase()) {
                    case "alert":
                        String atribs[] = data.split(",");
                        if (atribs.length > 1) {
                            switch (atribs[0].toLowerCase()) {
                                case "error":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.ERROR_MESSAGE);
                                    break;
                                case "warn":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.WARNING_MESSAGE);
                                    break;
                                case "info":
                                    JOptionPane.showMessageDialog(this, atribs[2], atribs[1],JOptionPane.INFORMATION_MESSAGE);
                                    break;
                            }
                        }else{
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
