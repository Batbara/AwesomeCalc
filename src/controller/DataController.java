package controller;

import model.CustomNode;
import model.Expression;
import view.TreeComponent;
import view.listeners.TreeListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class DataController {
    private Expression expression;
    private JTextPane screen;
    private TreeComponent treeComponent;

    public DataController() {
        expression = new Expression();
        screen = new JTextPane();
        treeComponent = new TreeComponent(screen.getWidth());
    }


    public TreeComponent getTreeComponent() {
        return treeComponent;
    }

    public void displayOnScreen(String stringToDisplay) throws BadLocationException {
        Document screenDoc = screen.getStyledDocument();
        screenDoc.remove(0, screenDoc.getLength());
        screenDoc.insertString(screenDoc.getLength(), stringToDisplay, null);
    }

    public void setUp(String input) {
        expression.setInfix(input);
        String postfix = ReversePolishNotation.convertToRPN(expression.getInfix());
        expression.setPostfix(postfix);

        CustomNode topNode = treeComponent.getTopNode();
        topNode = ReversePolishNotation.makeTree(expression.getPostfix(), topNode);
        treeComponent.setTopNode(topNode);
        treeComponent.updateRootStatus();
    }

    private void reloadTreeComponent() {
        CustomNode topNode = treeComponent.getTopNode();
        visualizeTree(topNode);
    }

    public void viewResult() {

        reloadTreeComponent();

        double calcResult = ReversePolishNotation.calculate(expression.getPostfix());
        try {
            treeComponent.viewResult(calcResult);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public void setPostfix(String postfix) {
        expression.setPostfix(postfix);
    }

    private void visualizeTree(CustomNode topNode) {
        treeComponent.setTopNode(topNode);
        treeComponent.updateComponent();
        treeComponent.expandAllNodes(0, treeComponent.getTree().getRowCount());
    }

    public void setScreen(JTextPane screen) {
        this.screen = screen;
        this.screen.setFont(new Font("Helvetica", Font.PLAIN, 19));
    }

    public void setTreeComponent(TreeComponent treeComponent) {
        this.treeComponent = treeComponent;

        this.treeComponent.getTree().addTreeSelectionListener(new TreeListener(this));
    }

}
