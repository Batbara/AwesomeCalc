package controller;

import model.Formula;
import view.AdvancedButtonsPanel;
import view.SimpleButtonsPanel;
import view.TreeComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataController {
    private Formula formula;
    private JTextPane screen;
    private SimpleButtonsPanel simpleButtonsPanel;
    private AdvancedButtonsPanel advancedButtonsPanel;
    private TreeComponent treeComponent;

    public DataController() {
        formula = new Formula();
        screen = new JTextPane();
        treeComponent = new TreeComponent(screen.getWidth());
        simpleButtonsPanel = new SimpleButtonsPanel();
        advancedButtonsPanel = new AdvancedButtonsPanel();
    }

    public void setScreen(JTextPane screen) {
        this.screen = screen;
        this.screen.setFont(new Font("Helvetica", Font.PLAIN, 19));
    }

    public void setTreeComponent(TreeComponent treeComponent) {
        this.treeComponent = treeComponent;
    }

    public void setSimpleButtonsPanel(SimpleButtonsPanel simpleButtonsPanel) {
        this.simpleButtonsPanel = simpleButtonsPanel;
    }

    public void setAdvancedButtonsPanel(AdvancedButtonsPanel advancedButtonsPanel) {
        this.advancedButtonsPanel = advancedButtonsPanel;
    }

    public void addSimpleButtonsListeners() {
        Map<String, JButton> simpleButtons = simpleButtonsPanel.getButtons();
        JButton clearButton = simpleButtonsPanel.getEraseButton();
        JButton resultButton = simpleButtonsPanel.getResultButton();
        Document screenDoc = screen.getStyledDocument();

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    screenDoc.remove(0, screenDoc.getLength());
                    treeComponent.clearScreen();
                    DefaultTreeModel treeModel = (DefaultTreeModel) treeComponent.getTree().getModel();
                    DefaultMutableTreeNode topNode = treeComponent.getTopNode();
                    topNode.removeAllChildren();
                    topNode.setUserObject("");
                    treeModel.reload(topNode);

                    formula.clearOperationList();
                } catch (BadLocationException e1) {
                    System.err.println("BadLocationException caught!");
                }
            }
        });
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formula.getOperationList().isEmpty() || !isInputValid()) {
                    return;
                }
                treeComponent.getTopNode().removeAllChildren();
                try {
                    makeTree(screenDoc);
                    treeComponent.expandAllNodes(0,treeComponent.getTree().getRowCount());
                    treeComponent.viewResult();
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });

     //   addDelButtonListener(simpleButtons, screenDoc);
        addOperationButtonsListener(simpleButtons, screenDoc);
        addFunctionButtonsListener(simpleButtons,screenDoc);

        for (String key : simpleButtons.keySet()) {
            JButton button = new JButton();
            if (!isOperation(key) && !key.equals("del") && !isFunction(key)) {
                button = simpleButtons.get(key);
            }
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try {
                        screenDoc.insertString(screenDoc.getLength(), getDisplayableName(key), null);
                    } catch (BadLocationException e1) {
                        System.err.println("BadLocationException caught!");
                    }
                    DefaultCaret caret = (DefaultCaret) screen.getCaret();
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                }
            });
        }
    }

    private void addDelButtonListener(Map<String, JButton> simpleButtons, Document screenDoc) {
        JButton delButton = simpleButtons.get("del");
        final StringBuffer[] buffer = {new StringBuffer()};
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String lastChar = screenDoc.getText(screenDoc.getLength() - 1, 1);
                    buffer[0].append(lastChar);
                    screenDoc.remove(screenDoc.getLength() - 1, 1);
                    if (isOperation(lastChar)) {
                        formula.removeLastOperation();
                        buffer[0] = new StringBuffer();
                    } else {
                        if (isNumber(buffer[0].toString())) {
                            buffer[0] = new StringBuffer();
                        }
                    }
                } catch (BadLocationException e1) {
                    System.err.println("BadLocationException caught!");
                }
                DefaultCaret caret = (DefaultCaret) screen.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            }
        });
    }

    private void addOperationButtonsListener(Map<String, JButton> simpleButtons, Document screenDoc) {
        for (String key : simpleButtons.keySet()) {
            JButton operationButton;
            if (isOperation(key) && !isFunction(key)) {
                operationButton = simpleButtons.get(key);
            } else continue;
            operationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (!checkLastSymbols(screenDoc) && !key.equals("(") && !key.equals(")")) {
                            int lastOperationLength = formula.getLastOperation().length();
                            screenDoc.remove(screenDoc.getLength() - lastOperationLength, lastOperationLength);
                            screen.repaint();
                            formula.removeLastOperation();
                        }
                        formula.addOperation(getDisplayableName(key));
                        screenDoc.insertString(screenDoc.getLength(), getDisplayableName(key), null);
                    } catch (BadLocationException e1) {
                        System.err.println("BadLocationException caught!");
                    }
                    DefaultCaret caret = (DefaultCaret) screen.getCaret();
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

                }
            });
        }
    }
    private void addFunctionButtonsListener(Map<String, JButton> simpleButtons, Document screenDoc) {
        for (String key : simpleButtons.keySet()) {
            JButton functionButton;
            if (isFunction(key)) {
                functionButton = simpleButtons.get(key);
            } else continue;
            functionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int numberIndex = getLastNumberIndex(screenDoc);
                    if (numberIndex == -1)
                        return;
                    int numberLength = screenDoc.getLength()-numberIndex;
                    try {
                        String functionString = key+"("+
                                screenDoc.getText(numberIndex,numberLength)
                                +")";
                        screenDoc.remove(numberIndex,numberLength);
                        screenDoc.insertString(screenDoc.getLength(),functionString, null);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }

                    formula.addOperation(key);
                }
            });
        }
    }
    private boolean isInputValid() {
        Document screenDoc = screen.getStyledDocument();
        String lastSymbol = null;
        try {
            lastSymbol = screenDoc.getText(screenDoc.getLength() - 1, 1);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException caught!");
        }
        if (formula.countBrackets("(") != formula.countBrackets(")"))
            return false;
        if (isOperation(lastSymbol)) {
            if (Objects.equals(lastSymbol, "(") || Objects.equals(lastSymbol, ")"))
                return true;
            return false;
        }
        return true;
    }

    private boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%",  "/", "*", "(", ")"};
        for (String operation : operationList) {
            if (operation.equals(key))
                return true;
        }
        return false;
    }
    private boolean isFunction(String key){
        String[] functionList = { "inv", "sqrt"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }
    private String getDisplayableName(String key) {
        switch (key) {
            case "mult":
                return "*";
            case "div":
                return "/";
            case "dot":
                return ".";
        }
        return key;
    }
    private int getLastNumberIndex(Document screenDoc){
        String text = null;
        try {
            text = screenDoc.getText(0,screenDoc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int lastNumberIndex = -1;
        for (int character = screenDoc.getLength()-1; character>=0; character--){
            String inputChar = String.valueOf(text.charAt(character));
            if(isNumber(inputChar) || inputChar.equals(".")){
                lastNumberIndex = character;
            }
            else
                break;

        }
        return lastNumberIndex;
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

    private boolean checkLastSymbols(Document screenDoc) throws BadLocationException {
        String[] operationList = {"mult", "+", "-", "div", "%", "x-1", "sqrt", "/", "*"};
        for (String possibleOperation : operationList) {
            int operationLength = possibleOperation.length();
            if (operationLength > screenDoc.getLength())
                continue;
            String lastSymbols = screenDoc.getText(screenDoc.getLength() - operationLength, operationLength);
            if (lastSymbols.equals("(") || lastSymbols.equals(")"))
                return true;
            if (isOperation(lastSymbols))
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
