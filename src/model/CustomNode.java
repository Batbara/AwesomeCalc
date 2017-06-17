package model;

import controller.ReversePolishNotation;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CustomNode extends DefaultMutableTreeNode {
    private String openStateName;
    private String closeStateName;
    private List<TreeNode> nodeChildren;


    public CustomNode(String name) {
        super(name);
        openStateName = null;
        closeStateName = null;
        nodeChildren = new ArrayList<>();
    }

    private void updateCloseStateName() {
        String postfix = ReversePolishNotation.parseIntoRPN(this);
        closeStateName = Double.toString(ReversePolishNotation.calculate(postfix));
    }

    public void updateState() {
        openStateName = (String) this.getUserObject();
        nodeChildren = new ArrayList<>();
        updateCloseStateName();
        Enumeration children = this.children();
        while (children.hasMoreElements()) {
            nodeChildren.add((CustomNode) children.nextElement());
        }
    }

    public void openNode() {
        this.setUserObject(openStateName);
        for (TreeNode child : nodeChildren) {
            this.add((CustomNode) child);
        }
    }

    public void closeNode() {

        this.removeAllChildren();
        this.setUserObject(closeStateName);
    }

}
