package view.listeners;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Batbara on 15.06.2017.
 */
public class SimpleButtonsListener implements ActionListener {

    private JTextPane screen;
    private StringBuffer output;
    public SimpleButtonsListener(JTextPane screen, StringBuffer output) {

        this.screen = screen;
        this.output = output;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Document screenDoc = screen.getStyledDocument();
        String key = e.getActionCommand();
        if (isFunction(key)){
            int numberIndex = getLastNumberIndex(screenDoc);
            if (numberIndex == -1)
                return;
            int numberLength = screenDoc.getLength()-numberIndex;
            try {
                String functionString = key+"("+
                        screenDoc.getText(numberIndex,numberLength)
                        +")";
                output.append(" ").append(key).append(" ");
                screenDoc.remove(numberIndex,numberLength);
                screenDoc.insertString(screenDoc.getLength(),functionString, null);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }else {
            try {
                if(isOperation(getDisplayableName(key))) {

                    output.append(" ").append(getDisplayableName(key)).append(" ");
                }
                else output.append(getDisplayableName(key));
                screenDoc.insertString(screenDoc.getLength(), getDisplayableName(key), null);
            } catch (BadLocationException e1) {
                System.err.println("BadLocationException caught!");
            }
        }
        DefaultCaret caret = (DefaultCaret) screen.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    }

    private boolean isFunction(String key) {
        String[] functionList = {"inv", "sqrt"};
        for (String function : functionList) {
            if (function.equals(key))
                return true;
        }
        return false;
    }
    private boolean isOperation(String key) {
        String[] operationList = {"+", "-", "mult", "div", "%",  "/", "*", "(", ")"};
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
    private boolean isNumber(String strToCheck) {
        try {
            Double.parseDouble(strToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
