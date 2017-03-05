/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jsontoui;

import java.awt.event.ActionListener;
import sun.font.Script;

/**
 *
 * @author Thanura
 */
public abstract class onclick implements ActionListener{
   public String script;
    public onclick(String script) {
        this.script = script;
    }
}
