/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import com.sun.corba.se.pept.transport.ContactInfo;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Thanura
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        // decide what icons you want by examining the node
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if(node.isRoot()){
                setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            }
            if(node.getUserObject().equals("UI") || node.getUserObject().equals("Scripts")){
                setIcon(UIManager.getIcon("FileView.directoryIcon"));
            }
            if (node.getUserObject() instanceof FileObject) {
                setIcon(UIManager.getIcon("FileView.fileIcon"));
            } 
        }
        return this;
    }

}
