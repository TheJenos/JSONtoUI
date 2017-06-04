/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class Sellection<T> {

    private T t;
    private String type;
    private JSONObject data;

    public Sellection(T t) {
        this.t = t;
        if (t instanceof SimpleImageView) {
            this.type = "Image";
        } else if (t instanceof JButton) {
            this.type = "Button";
        } else if (t instanceof JLabel) {
            this.type = "Lable";
        } else if (t instanceof JTextField) {
            this.type = "EditText";
        } else if (t instanceof SQL_Table) {
            this.type = "SQL_Table";
        }
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    
    public JSONObject getData() {
        return data;
    }

    
    public T getComponent() {
        return t;
    }

    public String getType() {
        return type;
    }

}
