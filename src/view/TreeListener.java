package view;

import view.TreeComponent;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Batbara on 15.06.2017.
 */
public class TreeListener implements TreeSelectionListener{
    private TreeComponent treeComponent;
    private JTree tree;
    public TreeListener(TreeComponent treeComponent){
        this.treeComponent = treeComponent;
        tree = this.treeComponent.getTree();
    }
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
        if (node == null) return;
        Object nodeInfo = node.getUserObject();
    }
}
