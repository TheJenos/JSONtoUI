/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TooManyListenersException;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.StrokeBorder;

/**
 *
 * @author Thanura
 */
public class SimpleImageView extends JLabel {
    
    private BufferedImage target;
    
    private File ImageFile;
    private int oldw, oldh;
    
    public File getImageFile() {
        return ImageFile;
    }
    
    private Runnable loadimge = new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (this) {
                    BufferedImage img = ImageIO.read(ImageFile);
                    target = img;
                    setIcon(new ImageIcon(img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
                    setText(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable resize = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (this) {
                        if (target != null) {
                            setIcon(new ImageIcon(target.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
                            setText(null);
                        }
                        Thread.sleep(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    
    public SimpleImageView() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setBackground(Color.WHITE);
        setText("No Image");
    }
    
    public void loadImage(final File f) {
        this.ImageFile = f;
        setIcon(null);
        setText("Loading...");
        new Thread(loadimge).start();
    }
    
    public void loadImageNOThread(final File f) {
        this.ImageFile = f;
        setIcon(null);
        setText("Loading...");
        try {
            BufferedImage img = ImageIO.read(ImageFile);
            target = img;
            setIcon(new ImageIcon(img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
            setText(null);
        } catch (Exception e) {
            System.out.println(f.getAbsolutePath());
            e.printStackTrace();
        }
        new Thread(resize).start();
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height); //To change body of generated methods, choose Tools | Templates.
        //System.out.println(target);

    }
    
    public void clear() {
        setIcon(null);
        setText("No Image");
    }
    
}
