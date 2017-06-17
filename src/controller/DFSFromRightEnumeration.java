package controller;

import javax.swing.tree.TreeNode;
import java.util.*;

public class DFSFromRightEnumeration implements Enumeration<TreeNode> {
    private TreeNode root;
    private Enumeration<TreeNode> children;
    private Enumeration<TreeNode> subtree;

    public DFSFromRightEnumeration(TreeNode rootNode) {
        super();
        root = rootNode;
        children = getRightLeftEnumeration(rootNode);
        subtree = Collections.emptyEnumeration();
    }
    private Enumeration<TreeNode> getRightLeftEnumeration(TreeNode node){
        List<TreeNode> rightLeftOrder = new ArrayList<>();
        Enumeration<TreeNode> leftRightOrder =node.children();
        while(leftRightOrder.hasMoreElements()){
            rightLeftOrder.add(leftRightOrder.nextElement());
        }
        Collections.reverse(rightLeftOrder);
        return new Vector(rightLeftOrder).elements();
    }
    public boolean hasMoreElements() {
        return root != null;
    }

    public TreeNode nextElement() {
        TreeNode retval;

        if (subtree.hasMoreElements()) {
            retval = subtree.nextElement();
        } else if (children.hasMoreElements()) {
            subtree = new DFSFromRightEnumeration(children.nextElement());
            retval = subtree.nextElement();
        } else {
            retval = root;
            root = null;
        }

        return retval;
    }
}
