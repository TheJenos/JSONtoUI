/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

/**
 *
 * @author Thanura
 */
public class JSONtoUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("-editor")) {
            new UIeditor().show();
        } else {
            JSONUI_File jf = new JSONUI_File(args[0]);
            UI_generator ui = new UI_generator(jf.getJSON(), null);
        }
    }

}
