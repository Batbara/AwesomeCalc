package controller;

import model.Expression;
import view.TreeComponent;
import view.listeners.TreeListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
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
        screenDoc.remove(0,screenDoc.getLength());
        screenDoc.insertString(screenDoc.getLength(),stringToDisplay, null);
    }
    public void createExpression(String input){
        expression.setInfix(input);
        String postfix = ReversePolishNotation.convertToRPN(expression.getInfix());
        expression.setPostfix(postfix);
    }


    public void viewResult(){

        DefaultMutableTreeNode topNode = treeComponent.getTopNode();

        String topNodeName = (String) topNode.getUserObject();
        if(topNodeName.equals("empty tree") || topNode.equals(""))
            topNode = ReversePolishNotation.makeTree(expression.getPostfix(), topNode);
        visualizeTree(topNode);

//        String expBack = ReversePolishNotation.parseIntoRPN(topNode);
//        System.out.println("exp back is: "+ expBack);
        double calcResult = ReversePolishNotation.calculate(expression.getPostfix());
        try {
            treeComponent.viewResult(calcResult);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

       // System.out.println(exp);

    }
    private void updateScreen(){
        String treeState = ReversePolishNotation.parseIntoRPN(treeComponent.getTopNode());

    }
    public void setPostfix(String postfix){
        expression.setPostfix(postfix);
    }
    public void setInfix(String infix) {
        expression.setInfix(infix);
        expression.setPostfix(ReversePolishNotation.convertToRPN(infix));
    }

    private void visualizeTree(DefaultMutableTreeNode topNode){
        //String postfix = expression.getPostfix();
        //topNode = ReversePolishNotation.makeTree(postfix, topNode);
       treeComponent.setTopNode(topNode);
        treeComponent.updateComponent();
        treeComponent.expandAllNodes(0,treeComponent.getTree().getRowCount());
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
