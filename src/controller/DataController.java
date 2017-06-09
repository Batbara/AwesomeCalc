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
                if (formula.getOperationList().isEmpty()) {
                    return;
                }
                formula.setTextDocument(screenDoc);
                treeComponent.getTopNode().removeAllChildren();
                try {
                    makeTree(screenDoc);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });

        addDelButtonListener(simpleButtons,screenDoc);
        addOperationButtonsListener(simpleButtons,screenDoc);

        for (String key : simpleButtons.keySet()) {
            JButton button  = new JButton();
            if(!isOperation(key) && !key.equals("del")) {
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
    private void addDelButtonListener(Map<String, JButton> simpleButtons, Document screenDoc){
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
            if(isOperation(key)) {
                 operationButton = simpleButtons.get(key);
            }
            else continue;
            operationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (!checkLastSymbols(screenDoc)) {
                            int lastOperationLength = formula.getLastOperation().length();
                            screenDoc.remove(screenDoc.getLength() - lastOperationLength, lastOperationLength);
                            screen.repaint();
                            formula.removeLastOperation();
                        }
                        formula.addOperation(getDisplayableName(key));
                        screenDoc.insertString(screenDoc.getLength(), getDisplayableName(key), null);
                    }catch (BadLocationException e1) {
                        System.err.println("BadLocationException caught!");
                    }
                    DefaultCaret caret = (DefaultCaret) screen.getCaret();
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                }
            });
        }
    }

    private boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%", "x-1", "sqrt", "/", "*", "(", ")"};
        for (String operation : operationList) {
            if (operation.equals(key))
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

    private void makeTree(Document screenDoc) throws BadLocationException {
        List<String> operationList = formula.getListWithoutBrackets();
        StringBuffer text = new StringBuffer(screenDoc.getText(0, screenDoc.getLength()));
        DefaultMutableTreeNode topNode = treeComponent.getTopNode();

        String topOperation = operationList.get(0);
        treeComponent.setTopNodeName(topOperation);
        topNode = formTree(topNode, formula.getOperandsOf(topOperation, text), operationList, 1);

        treeComponent.setTopNode(topNode);
        treeComponent.updateComponent();
    }

    private DefaultMutableTreeNode formTree(DefaultMutableTreeNode parent, String[] operands, List<String> operators, int count) {
        if (count == operators.size()) {
            for (String operand : operands)
                parent.add(new DefaultMutableTreeNode(operand));
            return parent;
        }
        for (String operand : operands) {
            StringBuffer operandBuffer = new StringBuffer(operand);
            if(operand.charAt(operand.length()-1)==')')
                operandBuffer = removeBrackets(operand);

            if (isNumber(operand)) {
                parent.add(new DefaultMutableTreeNode(operand));
            } else {
                String operator = operators.get(count);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(operator);
                count++;
                parent.add(formTree(node, formula.getOperandsOf(operator, operandBuffer), operators, count));
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
            if(lastSymbols.equals("(") || lastSymbols.equals(")"))
                return true;
            if (isOperation(lastSymbols))
                return false;
        }
        return true;
    }

    private StringBuffer removeBrackets(String inputString) {
        String withoutBrackets = inputString.replaceAll("[(*)*]","");
        StringBuffer inputStringBuffer = new StringBuffer(withoutBrackets);
        return inputStringBuffer;
    }
}
