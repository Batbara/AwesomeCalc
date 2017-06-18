package view;

import controller.DFSFromRightEnumeration;
import model.CustomNode;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class TreeComponent {
    private JTextPane resultScreen;
    private CustomNode topNode;
    private JTree tree;
    private JPanel treePanel;

    public TreeComponent(int width) {
        initScreen(width);
        topNode = new CustomNode("empty tree");
        tree = new JTree(topNode);
        JScrollPane calcTreeHolder = new JScrollPane(tree);
        initPanel();
        treePanel.add(new JScrollPane(resultScreen), BorderLayout.PAGE_START);
        treePanel.add(calcTreeHolder);
    }

    private void initScreen(int width) {
        resultScreen = new JTextPane();
        resultScreen.setSize(new Dimension(width, 50));
        resultScreen.setPreferredSize(resultScreen.getSize());
        resultScreen.setEditable(false);
        resultScreen.setBackground(Color.decode("#E9FAFD"));
        resultScreen.setBorder(BorderFactory.createLoweredBevelBorder());
        resultScreen.setFont(new Font("Helvetica", Font.PLAIN, 22));
        DefaultCaret caret = (DefaultCaret) resultScreen.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    }

    private void initPanel() {
        treePanel = new JPanel(new BorderLayout());
        treePanel.setPreferredSize(new Dimension(150, treePanel.getHeight()));
    }

    JPanel getTreePanel() {
        return treePanel;
    }

    void clearScreen() throws BadLocationException {
        Document screenDoc = resultScreen.getStyledDocument();
        screenDoc.remove(0, screenDoc.getLength());
    }

    public CustomNode getTopNode() {
        return topNode;
    }

    public void expandAllNodes(int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(rowCount, tree.getRowCount());
        }
    }

    public void viewResult(double result) throws BadLocationException {
        clearScreen();
        Document screenDoc = resultScreen.getStyledDocument();

        screenDoc.insertString(0, Double.toString(result), null);
    }

    public void updateComponent() {

        treePanel.repaint();
        tree.repaint();

    }

    public void updateRootStatus() {
        topNode.updateState();
        DFSFromRightEnumeration allNodes = new DFSFromRightEnumeration(topNode);
        while (allNodes.hasMoreElements()) {
            CustomNode node = (CustomNode) allNodes.nextElement();
            node.updateState();
        }
    }

    public void setTopNode(CustomNode topNode) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        this.topNode = topNode;
        model.setRoot(topNode);
        model.reload(topNode);
        treePanel.repaint();
    }

    public JTree getTree() {
        return tree;
    }
}
