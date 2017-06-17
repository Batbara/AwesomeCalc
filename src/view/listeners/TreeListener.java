package view.listeners;

import controller.DataController;
import controller.ReversePolishNotation;
import model.CustomNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;


public class TreeListener implements TreeSelectionListener {
    private DataController dataController;
    private JTree tree;

    public TreeListener(DataController dataController) {
        this.dataController = dataController;
        tree = this.dataController.getTreeComponent().getTree();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        CustomNode node = (CustomNode)
                tree.getLastSelectedPathComponent();
        CustomNode topNode = dataController.getTreeComponent().getTopNode();

        if (node == null) return;
        if (node.isLeaf()) {
            node.openNode();
        } else {
            node.closeNode();
        }
        String treePostfix = ReversePolishNotation.parseIntoRPN(topNode);
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
