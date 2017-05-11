/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author AssHunter
 */
public class Utils {

    public static String SimpleEncrypt(String txt) throws Exception {
        String newtxt = txt;
        newtxt = newtxt.replace("'", "''");
//        newtxt = newtxt.replace("\\n", "[br]");
//        newtxt = newtxt.replace("?", "[qm]");
//        newtxt = newtxt.replace("!", "[em]");
//        newtxt = newtxt.replace("%", "[pm]");
//        newtxt = newtxt.replace("@", "[at]");
//        newtxt = newtxt.replace("'", "[sc]");
//        newtxt = newtxt.replace("=", "[eq]");
//        newtxt = newtxt.replace("?", "[qm]");
//        newtxt = newtxt.replace("\"", "[dc]");
//        newtxt = newtxt.replace("#", "[ht]");
//        newtxt = newtxt.replace("$", "[ds]");
        return newtxt;
    }

    public static String SimpleDecrypt(String txt) throws Exception {
        if (txt == null) {
            return null;
        }
        String newtxt = txt;
//        newtxt = newtxt.replace("[br]", "\n");
//        newtxt = newtxt.replace("[br]", "\n");
//        newtxt = newtxt.replace("[at]", "@");
//        newtxt = newtxt.replace("[em]", "!");
//        newtxt = newtxt.replace("[sc]", "'");
//        newtxt = newtxt.replace("[eq]", "=");
//        newtxt = newtxt.replace("[qm]", "?");
//        newtxt = newtxt.replace("[ht]", "#");
//        newtxt = newtxt.replace("[ds]", "$");
//        newtxt = newtxt.replace("[dc]", "\"");
        return newtxt;
    }

    public static void ClearAll(final Container c) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof Container) {
                ClearAll((Container) comp);
            }
            if (comp instanceof JTextComponent) {
                JTextComponent jl = (JTextComponent) comp;
                jl.setText(null);
            }

        }

    }

    public static void Refresh(JTable j, Runnable r) {
        DefaultTableModel dtm = (DefaultTableModel) j.getModel();
        dtm.setRowCount(0);
        r.run();
    }


    public static String showInputDialog() {
        JPasswordField pf = new JPasswordField();
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            String password = new String(pf.getPassword());
            return password;
        }
        return "";
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

  
    public static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();
    public static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};

    public static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    public static String base64Encode(byte[] bytes) {
        // NB: This class is internal, and you probably should use another impl
        return new BASE64Encoder().encode(bytes);
    }

    public static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    public static byte[] base64Decode(String property) throws IOException {
        // NB: This class is internal, and you probably should use another impl
        return new BASE64Decoder().decodeBuffer(property);
    }

    public static void type(String characters) throws Exception {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(characters);
        clipboard.setContents(stringSelection, null);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

 

  
    public static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            System.out.println(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();

    }

    static public String ExportResource(Class c, String resourceName, String name) throws Exception {
        String jarFolder = null;
        try {
            InputStream stream = c.getResourceAsStream(resourceName + "/" + name);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            OutputStream resStreamOut = new FileOutputStream(jarFolder + name);
            System.out.println(jarFolder);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            stream.close();
            resStreamOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jarFolder + name;
    }
}
