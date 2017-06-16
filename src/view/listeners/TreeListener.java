package view.listeners;

import controller.DataController;
import controller.ReversePolishNotation;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Batbara on 15.06.2017.
 */
public class TreeListener implements TreeSelectionListener{
    private DataController dataController;
    private JTree tree;
    public TreeListener(DataController dataController){
        this.dataController = dataController;
        tree = this.dataController.getTreeComponent().getTree();
    }
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
        if (node == null || node.isLeaf()) return;

        String nodeInRPN = ReversePolishNotation.parseIntoRPN(node);
        double nodeCalculation = ReversePolishNotation.calculate(nodeInRPN);
        node.removeAllChildren();
        node.setUserObject(Double.toString(nodeCalculation));
       // DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        //model.reload(node);
        String treePostfix = ReversePolishNotation.parseIntoRPN(dataController.getTreeComponent().getTopNode());
        dataController.setPostfix(treePostfix);
        String viewString = ReversePolishNotation.convertToRawString(treePostfix);
        try {
            dataController.displayOnScreen(viewString);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        dataController.viewResult();
    }
}
