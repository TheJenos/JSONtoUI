/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thanura
 */
public class UIeditor extends javax.swing.JFrame {

    /**
     * Creates new form UIeditor
     */
//    String sellected_component_type;
//    JLabel sellected_component_Jlable;
//    JButton sellected_component_JButton;
//    JTextField sellected_component_JTextField;
    Sellection<Component> sellected;
    JSONUI_File jf = new JSONUI_File();

    MouseAdapter click = new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            clear_sellection();
            sellected = new Sellection<Component>(me.getComponent());
            updatePorperties();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            updatePorperties(); //To change body of generated methods, choose Tools | Templates.
        }

    };
    boolean js1, js2, js4, js3, js5;

    public UIeditor() {
        initComponents();
    }

    void updatePorperties() {
        Component c = sellected.getComponent();
        switch (sellected.getType()) {
            case "Lable":
                jTextArea2.setText(((JLabel) sellected.getComponent()).getText());
                ((JLabel) sellected.getComponent()).setBorder(new LineBorder(Color.BLACK, 1));
                break;
            case "Button":
                jTextArea2.setText(((JButton) sellected.getComponent()).getText());
                ((JButton) sellected.getComponent()).setBorder(new LineBorder(Color.BLACK, 1));
                break;
            case "EditText":
                jTextArea2.setText(((JTextField) sellected.getComponent()).getText());
                if (!(((JTextField) sellected.getComponent()).getBorder() instanceof CompoundBorder)) {
                    CompoundBorder cb = new CompoundBorder(new LineBorder(Color.BLACK, 1), ((JTextField) sellected.getComponent()).getBorder());
                    ((JTextField) sellected.getComponent()).setBorder(cb);
                }
                break;
            case "Image":
                jTextArea2.setText(((SimpleImageView) sellected.getComponent()).getImageFile().getAbsolutePath());
                ((SimpleImageView) sellected.getComponent()).setBorder(new LineBorder(Color.BLACK, 1));
                break;
        }
        try {
            jSlider5.setMaximum(jPanel4.getComponentCount() - 1);
            jSlider5.setValue(jPanel4.getComponentZOrder(c));
            jLabel3.setText("<html>Z-index<br>" + jPanel4.getComponentZOrder(c));
            jLabel4.setText("<html>X<br>" + c.getX());
            jSlider3.setValue((int) c.getX());
            jSlider4.setValue((int) c.getY());
            jLabel5.setText("<html>Y<br>" + c.getY());
            sellected.setData(new JSONObject(c.getName()));
            jTextArea3.setText(sellected.getData().isNull("onclick")?"":sellected.getData().getString("onclick"));
            jCheckBox1.setSelected(sellected.getData().isNull("resize")?false:sellected.getData().getBoolean("resize"));
            jLabel9.setText("<html>Width<br>" + c.getPreferredSize().getWidth());
            jSlider1.setValue((int) c.getPreferredSize().getWidth());
            jSlider2.setValue((int) c.getPreferredSize().getHeight());
            jLabel8.setText("<html>Height<br>" + c.getPreferredSize().getHeight());
            update_component();
        } catch (NullPointerException e) {
        }
        JSONcreate();
    }

    void JSONcreate() {
        Component[] list = jPanel4.getComponents();
        JSONObject jo = new JSONObject();
        jo.put("width", jPanel4.getWidth());
        jo.put("height", jPanel4.getHeight());
        jo.put("packed", false);
        jo.put("text", "Testing");
        JSONArray ja = new JSONArray();
        for (Component component : list) {
            JSONObject jo1 = new JSONObject();
            if (component instanceof SimpleImageView) {
                SimpleImageView jl = (SimpleImageView) component;
                jo1.put("x", jl.getX());
                jo1.put("y", jl.getY());
                jo1.put("z", jPanel4.getComponentZOrder(jl));
                jo1.put("width", jl.getWidth());
                jo1.put("height", jl.getHeight());
                jo1.put("path", jl.getImageFile().getAbsoluteFile());
                jo1.put("data", new JSONObject(jl.getName()));
                jo1.put("type", "Image");
            } else if (component instanceof JLabel) {
                JLabel jl = (JLabel) component;
                jo1.put("x", jl.getX());
                jo1.put("y", jl.getY());
                jo1.put("z", jPanel4.getComponentZOrder(jl));
                jo1.put("width", jl.getWidth());
                jo1.put("height", jl.getHeight());
                jo1.put("text", jl.getText());
                jo1.put("data", new JSONObject(jl.getName()));
                jo1.put("type", "Lable");
            } else if (component instanceof JButton) {
                JButton jb = (JButton) component;
                jo1.put("x", jb.getX());
                jo1.put("y", jb.getY());
                jo1.put("z", jPanel4.getComponentZOrder(jb));
                jo1.put("width", jb.getWidth());
                jo1.put("height", jb.getHeight());
                jo1.put("text", jb.getText());
                jo1.put("data", new JSONObject(jb.getName()));
                jo1.put("type", "Button");
            } else if (component instanceof JTextField) {
                JTextField jb = (JTextField) component;
                jo1.put("x", jb.getX());
                jo1.put("y", jb.getY());
                jo1.put("z", jPanel4.getComponentZOrder(jb));
                jo1.put("width", jb.getWidth());
                jo1.put("height", jb.getHeight());
                jo1.put("text", jb.getText());
                jo1.put("data", new JSONObject(jb.getName()));
                jo1.put("type", "EditText");
            }
            ja.put(jo1);
        }
        jo.put("UI", ja);
        jTextArea1.setText(jo.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JLayeredPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jSlider3 = new javax.swing.JSlider();
        jSlider4 = new javax.swing.JSlider();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jSlider5 = new javax.swing.JSlider();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("UI Editor");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Editable Space"));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setOpaque(true);
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Components"));

        jLabel1.setText("Caption (Text/Image Path) ");

        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Component Type");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Lable", "Button", "EditText", "Image" }));

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(12, 12, 12))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("JSON"));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jButton4.setText("Save As");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Load JSON");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Save JSON");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton7)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Properties"));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel5MouseExited(evt);
            }
        });

        jLabel4.setText("X");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Y");

        jLabel7.setText("Caption (Text/Image Path) ");

        jLabel8.setText("Height");

        jLabel9.setText("Width");

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSlider1.setMaximum(1000);
        jSlider1.setValue(0);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSlider1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSlider1MouseExited(evt);
            }
        });
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jSlider2.setMaximum(1000);
        jSlider2.setValue(0);
        jSlider2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSlider2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSlider2MouseExited(evt);
            }
        });
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jSlider3.setMaximum(1000);
        jSlider3.setValue(0);
        jSlider3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSlider3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSlider3MouseExited(evt);
            }
        });
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });

        jSlider4.setMaximum(1000);
        jSlider4.setValue(0);
        jSlider4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSlider4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSlider4MouseExited(evt);
            }
        });
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea2);

        jButton2.setText("Make UI");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setText("OnClick");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea3KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTextArea3);

        jLabel3.setText("Z-index");

        jSlider5.setMaximum(1000);
        jSlider5.setValue(0);
        jSlider5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSlider5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSlider5MouseExited(evt);
            }
        });
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });

        jButton6.setText("Add Path");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Resize With Frame");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSlider5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSlider3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSlider5, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String selected_type = jComboBox1.getSelectedItem().toString();
        if (selected_type.equals("Lable")) {
            JLabel jl = new JLabel();
            jl.setText(jTextField1.getText());
            jTextField1.setText("");
            jl.setHorizontalAlignment(JLabel.CENTER);
            jPanel4.add(jl);
            jl.setName(new JSONObject().toString());
            jl.setBounds(10, 10, 30, 30);
            jl.addMouseListener(click);
            new ComponentMover().registerComponent(jl);
            jPanel4.setComponentZOrder(jl, 0);
            jPanel4.revalidate();
        } else if (selected_type.equals("Button")) {
            JButton jb = new JButton();
            jb.setText(jTextField1.getText());
            jTextField1.setText("");
            jPanel4.add(jb);
            jb.setName(new JSONObject().toString());
            jb.setBounds(10, 10, 30, 30);
            jb.addMouseListener(click);
            jPanel4.setComponentZOrder(jb, 0);
            new ComponentMover().registerComponent(jb);
            jPanel4.revalidate();
        } else if (selected_type.equals("EditText")) {
            JTextField jt = new JTextField();
            jt.setText(jTextField1.getText());
            jTextField1.setText("");
            jPanel4.add(jt);
            jt.setName(new JSONObject().toString());
            jt.setBounds(10, 10, 30, 30);
            jt.addMouseListener(click);
            jPanel4.setComponentZOrder(jt, 0);
            new ComponentMover().registerComponent(jt);
            jPanel4.revalidate();
        } else if (selected_type.equals("Image")) {
            SimpleImageView img = new SimpleImageView();
            img.loadImage(new File(jTextField1.getText()));
            jTextField1.setText("");
            jPanel4.add(img);
            img.setName(new JSONObject().toString());
            img.setBounds(10, 10, 30, 30);
            img.addMouseListener(click);
            img.setBorder(null);
            jPanel4.setComponentZOrder(img, 0);
            new ComponentMover().registerComponent(img);
            jPanel4.revalidate();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        jSlider1.setMaximum(jPanel4.getWidth());
        jSlider2.setMaximum(jPanel4.getHeight());
        jSlider3.setMaximum(jPanel4.getWidth());
        jSlider4.setMaximum(jPanel4.getHeight());
    }//GEN-LAST:event_formComponentResized

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        jLabel9.setText("<html>Width<br>" + jSlider1.getValue());
        if (js1) {
            update_component();
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        jLabel8.setText("<html>Height<br>" + jSlider2.getValue());
        if (js2) {
            update_component();
        }
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        jLabel4.setText("<html>X<br>" + jSlider3.getValue());
        if (js3) {
            update_component();
        }
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        jLabel5.setText("<html>Y<br>" + jSlider4.getValue());
        if (js4) {
            update_component();
        }
    }//GEN-LAST:event_jSlider4StateChanged

    private void jTextArea2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea2KeyReleased
        update_component();
    }//GEN-LAST:event_jTextArea2KeyReleased

    private void jSlider4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider4MouseEntered
        js4 = true;
    }//GEN-LAST:event_jSlider4MouseEntered

    private void jSlider4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider4MouseExited
        js4 = false;
    }//GEN-LAST:event_jSlider4MouseExited

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered

    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited

    }//GEN-LAST:event_jPanel5MouseExited

    private void jSlider3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider3MouseEntered
        js3 = true;
    }//GEN-LAST:event_jSlider3MouseEntered

    private void jSlider3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider3MouseExited
        js3 = false;
    }//GEN-LAST:event_jSlider3MouseExited

    private void jSlider1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseEntered
        js1 = true;
    }//GEN-LAST:event_jSlider1MouseEntered

    private void jSlider1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseExited
        js4 = false;
    }//GEN-LAST:event_jSlider1MouseExited

    private void jSlider2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider2MouseEntered
        js2 = true;
    }//GEN-LAST:event_jSlider2MouseEntered

    private void jSlider2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider2MouseExited
        js2 = false;
    }//GEN-LAST:event_jSlider2MouseExited

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Component c = sellected.getComponent();
        jPanel4.remove(c);
        clear_sellection();
        jPanel4.revalidate();
        jPanel4.repaint();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        UI_generator ui = new UI_generator(jTextArea1.getText(), null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextArea3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea3KeyReleased
        update_component();
    }//GEN-LAST:event_jTextArea3KeyReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        String selected_type = jComboBox1.getSelectedItem().toString();
        if (selected_type.equals("Image")) {
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpge"));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                jTextField1.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jSlider5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider5MouseEntered
        js5 = true;
    }//GEN-LAST:event_jSlider5MouseEntered

    private void jSlider5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider5MouseExited
        js5 = false;
    }//GEN-LAST:event_jSlider5MouseExited

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged
        jLabel3.setText("<html>Z-index<br>" + jSlider5.getValue());
        if (js5) {
            update_component();
        }
    }//GEN-LAST:event_jSlider5StateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON Files", "JSON", "txt", "json"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            jf = new JSONUI_File(fc.getSelectedFile().getAbsolutePath());
            jTextArea1.setText(jf.getJSON());
            setTitle("UI Editor - " + fc.getSelectedFile().getAbsolutePath());
            loadFromJSON(jf.getJSON(), jPanel4);
            clear_sellection();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON Files", "JSON", "txt", "json"));
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String name = fc.getSelectedFile().getAbsolutePath();
            String realname = (name.substring(name.length() - 4, name.length()).toLowerCase().equals("json")) ? name : fc.getSelectedFile().getAbsolutePath() + ".json";
            jf = new JSONUI_File(realname);
            jf.setJSON(jTextArea1.getText());
            jf.saveFile();
            JOptionPane.showMessageDialog(this, "<html>File Saved<br>" + jf.getJSON_File().getAbsolutePath());
            clear_sellection();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON Files", "JSON", "txt", "json"));
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            jTextArea3.setText(jTextArea3.getText() + fc.getSelectedFile().getAbsolutePath());
        };
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        clear_sellection();
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (jf.getJSON_File() != null) {
            jf.setJSON(jTextArea1.getText());
            jf.saveFile();
            setTitle("UI Editor - " + jf.getJSON_File().getAbsolutePath() + " (Saved " + (new Date().toString()) + ")");
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON Files", "JSON", "txt", "json"));
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String name = fc.getSelectedFile().getAbsolutePath();
                String realname = (name.substring(name.length() - 4, name.length()).toLowerCase().equals("json")) ? name : fc.getSelectedFile().getAbsolutePath() + ".json";
                jf = new JSONUI_File(realname);
                jf.setJSON(jTextArea1.getText());
                jf.saveFile();
                JOptionPane.showMessageDialog(this, "<html>File Saved<br>" + jf.getJSON_File().getAbsolutePath());
                clear_sellection();
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        update_component();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTextArea1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyReleased
        loadFromJSON(jTextArea1.getText(), jPanel4);
    }//GEN-LAST:event_jTextArea1KeyReleased

    void clear_sellection() {
        Component[] list = jPanel4.getComponents();
        for (Component component : list) {
            if (component instanceof SimpleImageView) {
                SimpleImageView jl = (SimpleImageView) component;
                jl.setBorder(null);
            } else if (component instanceof JLabel) {
                JLabel jl = (JLabel) component;
                jl.setBorder(null);
            } else if (component instanceof JTextField) {
                JTextField jb = (JTextField) component;
                if (jb.getBorder() instanceof CompoundBorder) {
                    jb.setBorder(((CompoundBorder) jb.getBorder()).getInsideBorder());
                }
            } else if (component instanceof JButton) {
                JButton jb = (JButton) component;
                jb.setBorder(null);
            }
        }
        sellected = null;
        jSlider1.setValue(0);
        jSlider2.setValue(0);
        jSlider3.setValue(0);
        jSlider4.setValue(0);
        jSlider5.setValue(0);
        jTextArea3.setText("");
        jTextArea2.setText("");
        JSONcreate();
    }

    void update_component() {
        try {
            Component c = sellected.getComponent();
            switch (sellected.getType()) {
                case "Lable":
                    ((JLabel) sellected.getComponent()).setText(jTextArea2.getText());
                    break;
                case "Button":
                    ((JButton) sellected.getComponent()).setText(jTextArea2.getText());
                    break;
                case "EditText":
                    ((JTextField) sellected.getComponent()).setText(jTextArea2.getText());
                    break;
                case "Image":
                    File f = ((SimpleImageView) sellected.getComponent()).getImageFile();
                    if (jTextArea2.getText().equals(f.getAbsolutePath())) {
                        break;
                    }
                    System.out.println("Form Here");
                    ((SimpleImageView) sellected.getComponent()).loadImage(new File(jTextArea2.getText()));
                    break;
            }
            if (jSlider1.getValue() > 10 && jSlider2.getValue() > 10) {
                c.setPreferredSize(new Dimension(jSlider1.getValue(), jSlider2.getValue()));
                c.setSize(new Dimension(jSlider1.getValue(), jSlider2.getValue()));
                c.setLocation(jSlider3.getValue(), jSlider4.getValue());
                jPanel4.setComponentZOrder(c, jSlider5.getValue());
                JSONObject jo = sellected.getData();
                jo.put("onclick", jTextArea3.getText());
                jo.put("resize", jCheckBox1.isSelected());
                c.setName(jo.toString());
            }
        } catch (NullPointerException e) {
        }
        JSONcreate();
    }

    void loadFromJSON(String JSON, Container cnt) {
        cnt.removeAll();
        cnt.repaint();
        JSONObject obj = new JSONObject(JSON);
        JSONArray arr = obj.getJSONArray("UI");
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
                JLabel jl = new JLabel(text);
                cnt.add(jl);
                jl.setName(cdata.toString());
                cnt.setComponentZOrder(jl, z);
                jl.setBounds(x, y, uwidth, uheight);
                new ComponentMover().registerComponent(jl);
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.addMouseListener(click);
            } else if (type_ui.toLowerCase().equals("button")) {
                String text = arr.getJSONObject(i).getString("text");
                JButton jl = new JButton(text);
                jl.setName(cdata.toString());
                cnt.add(jl);
                cnt.setComponentZOrder(jl, z);
                jl.setBounds(x, y, uwidth, uheight);
                new ComponentMover().registerComponent(jl);
                jl.setPreferredSize(new Dimension(uwidth, uheight));
                jl.addMouseListener(click);
            } else if (type_ui.toLowerCase().equals("edittext")) {
                String text = arr.getJSONObject(i).getString("text");
                JTextField jt = new JTextField(text);
                cnt.add(jt);
                jt.setName(cdata.toString());
                cnt.setComponentZOrder(jt, z);
                jt.setBounds(x, y, uwidth, uheight);
                new ComponentMover().registerComponent(jt);
                jt.setPreferredSize(new Dimension(uwidth, uheight));
                jt.addMouseListener(click);
            } else if (type_ui.toLowerCase().equals("image")) {
                String text = arr.getJSONObject(i).getString("path");
                SimpleImageView img = new SimpleImageView();
                cnt.add(img);
                img.setName(cdata.toString());
                img.setBounds(x, y, uwidth, uheight);
                cnt.setComponentZOrder(img, z);
                new ComponentMover().registerComponent(img);
                img.setPreferredSize(new Dimension(uwidth, uheight));
                img.loadImageNOThread(new File(text));
                img.addMouseListener(click);
            }
            cnt.revalidate();
            cnt.repaint();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UIeditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UIeditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UIeditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UIeditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UIeditor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLayeredPane jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
