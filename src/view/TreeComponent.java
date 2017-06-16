package view;

import controller.DataController;
import view.listeners.TreeListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class TreeComponent {
    private JTextPane resultScreen;
    private DefaultMutableTreeNode topNode;
    private JTree tree;
    private JPanel treePanel;

    public TreeComponent(int width){
        initScreen(width);
        topNode = new DefaultMutableTreeNode("empty tree");
        tree = new JTree(topNode);
        JScrollPane calcTreeHolder = new JScrollPane(tree);
        initPanel();
        treePanel.add(resultScreen, BorderLayout.PAGE_START);
        treePanel.add(calcTreeHolder);
    }
    private void initScreen(int width){
        resultScreen = new JTextPane();
        resultScreen.setSize(new Dimension(width,50));
        resultScreen.setPreferredSize(resultScreen.getSize());
        resultScreen.setEditable(false);
        resultScreen.setBackground(Color.decode("#E9FAFD"));
        resultScreen.setBorder(BorderFactory.createLoweredBevelBorder());
        resultScreen.setFont(new Font("Helvetica", Font.PLAIN, 22));
    }
    private void initPanel(){
        treePanel = new JPanel(new BorderLayout());
        treePanel.setPreferredSize(new Dimension(150, treePanel.getHeight()));
    }

    public JPanel getTreePanel() {
        return treePanel;
    }
    public void clearScreen() throws BadLocationException {
        Document screenDoc = resultScreen.getStyledDocument();
        screenDoc.remove(0,screenDoc.getLength());
    }
    public DefaultMutableTreeNode getTopNode() {
        return topNode;
    }
    public void expandAllNodes( int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            expandAllNodes( rowCount, tree.getRowCount());
        }
    }

    public void viewResult(double result) throws BadLocationException {
        clearScreen();
        Document screenDoc = resultScreen.getStyledDocument();

        screenDoc.insertString(0,Double.toString(result),null);
    }

    public void updateComponent(){

        treePanel.repaint();
        tree.repaint();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();

    }
    public void setTopNode(DefaultMutableTreeNode topNode) {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = topNode;
        this.topNode = root;
        model.setRoot(root);
        model.reload(root);
        treePanel.repaint();
    }

    public JTree getTree() {
        return tree;
    }
}
