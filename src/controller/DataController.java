package controller;

import model.Formula;
import view.TreeComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class DataController {
    private Formula formula;
    private String expression;
    private JTextPane screen;
    private TreeComponent treeComponent;

    public DataController() {
        formula = new Formula();
        expression = null;
        screen = new JTextPane();
        treeComponent = new TreeComponent(screen.getWidth());

    }

    public void setExpression(String expression){
        this.expression = expression;
    }
    public void viewResult(){
        ReversePolishNotation notation = new ReversePolishNotation(treeComponent);
        String postfix = notation.convertToRPN(expression);
        DefaultMutableTreeNode topNode = treeComponent.getTopNode();
        topNode = notation.makeTree(postfix, topNode);
        treeComponent.setTopNode(topNode);
        treeComponent.updateComponent();
        treeComponent.expandAllNodes(0,treeComponent.getTree().getRowCount());
        double calcResult = notation.calculate(postfix);
        try {
            treeComponent.viewResult(calcResult);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        System.out.println(postfix);


    }
    public void setScreen(JTextPane screen) {
        this.screen = screen;
        this.screen.setFont(new Font("Helvetica", Font.PLAIN, 19));
    }

    public void setTreeComponent(TreeComponent treeComponent) {
        this.treeComponent = treeComponent;
    }

    private boolean isFunction(String key){
        String[] functionList = { "inv", "sqrt"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }


    private void makeTree(Document screenDoc) throws BadLocationException {
        List<String> operationList = formula.getListWithoutBrackets();
        StringBuffer text = new StringBuffer(screenDoc.getText(0, screenDoc.getLength()));
        text = removeBrackets(text.toString());
        DefaultMutableTreeNode topNode = treeComponent.getTopNode();

        String topOperation = operationList.get(0);
        treeComponent.setTopNodeName(topOperation);
        topNode = formTree(topNode, formula.getOperandsOf(topOperation, text), operationList, 1);

        treeComponent.setTopNode(topNode);
        treeComponent.updateComponent();
    }

    private DefaultMutableTreeNode formTree(DefaultMutableTreeNode parent, String[] operands, List<String> operators, int count) {
        if (count == operators.size()) {
            for (String operand : operands) {
                StringBuffer operandBuffer = new StringBuffer(operand);
                parent.add(new DefaultMutableTreeNode(operandBuffer.toString()));
            }
            return parent;
        }
        for (String operand : operands) {
            StringBuffer operandBuffer = new StringBuffer(operand);

            if (isNumber(operand)) {
                parent.add(new DefaultMutableTreeNode(operand));
            } else {

                String operator = operators.get(count);
                count++;
                if (isFunction(operator)){
                    String functionValue = formula.getFunctionValue(operator,operandBuffer);
                    DefaultMutableTreeNode functionNode = new DefaultMutableTreeNode(operator);
                    functionNode.add(new DefaultMutableTreeNode(functionValue));
                    parent.add(functionNode);
                }
                else {
                    String[] subOperands = formula.getOperandsOf(operator, operandBuffer);
                    while (subOperands.length == 1) {
                        if(count >= operators.size()-1)
                         count++;
                        else
                            break;
                        operator = operators.get(count);
                        subOperands = formula.getOperandsOf(operator, operandBuffer);
                    }

                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(operator);
                        parent.add(formTree(node, subOperands, operators, count));

                }
            }
        }

        return parent;
    }

    private boolean isNumber(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }



    private StringBuffer removeBrackets(String inputString) {
        String withoutBrackets = inputString.replaceAll("[(]", "");
        withoutBrackets = withoutBrackets.replaceAll("[)]", "");
        StringBuffer inputStringBuffer = new StringBuffer(withoutBrackets);
        return inputStringBuffer;
    }
}
