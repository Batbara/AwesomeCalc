package view;

import javax.swing.*;
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
        topNode = new DefaultMutableTreeNode("none");
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
    }
    private void initPanel(){
        treePanel = new JPanel(new BorderLayout());
        treePanel.setPreferredSize(new Dimension(150, treePanel.getHeight()));
    }

    public JPanel getTreePanel() {
        return treePanel;
    }

    public DefaultMutableTreeNode getTopNode() {
        return topNode;
    }

    public void setTopNodeName(String topNodeName){
        topNode.setUserObject(topNodeName);
        treePanel.repaint();
    }
    public void addChild(DefaultMutableTreeNode child){
        topNode.add(child);
    }

    public void updateComponent(){

        treePanel.repaint();
        tree.repaint();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();

    }
    public void setTopNode(DefaultMutableTreeNode topNode) {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode root = topNode;
        model.reload(root);
        treePanel.repaint();
    }

    public JTree getTree() {
        return tree;
    }
}
