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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
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
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document screenDoc = screen.getStyledDocument();
                try {
                    screenDoc.remove(0, screenDoc.getLength());
                    treeComponent.getTopNode().removeAllChildren();
                    formula.clearOperationList();
                } catch (BadLocationException e1) {
                    System.err.println("BadLocationException caught!");
                }
            }
        });
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document screenDoc = screen.getStyledDocument();
                formula.setTextDocument(screenDoc);
                treeComponent.getTopNode().removeAllChildren();
                try {
                    makeTree(screenDoc);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                //formula.clearOperationList();
            }
        });
        for (String key : simpleButtons.keySet()) {
            JButton button = simpleButtons.get(key);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // formula.addElement(key);
                    screen.setFont(new Font("Helvetica", Font.PLAIN, 19));
                    Document screenDoc = screen.getStyledDocument();
                    try {
                        if (key.equals("del")) {
                            screenDoc.remove(screenDoc.getLength() - 1, 1);
                            if (isOperation(screenDoc.getText(screenDoc.getLength(), 1))) {
                                formula.removeLastOperation();
                            }
                        } else {
                            screenDoc.insertString(screenDoc.getLength(), getDisplayableName(key), null);
                            if (isOperation(key))
                                formula.addOperation(getDisplayableName(key));
                        }
                    } catch (BadLocationException e1) {
                        System.err.println("BadLocationException caught!");
                    }
                    DefaultCaret caret = (DefaultCaret) screen.getCaret();
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                }
            });
        }
    }

    private boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%", "x-1", "sqrt"};
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
        List<String> operationList = formula.getOperationList();
        String text = screenDoc.getText(0, screenDoc.getLength());
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
        String operator = operators.get(count);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(operator);
        count++;
        for (String operand : operands) {
            if (isInteger(operand)) {
                parent.add(new DefaultMutableTreeNode(operand));
            } else {
                parent.add(formTree(node, formula.getOperandsOf(operator, operand), operators, count));
            }
        }

        return parent;
    }

    private boolean isInteger(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
